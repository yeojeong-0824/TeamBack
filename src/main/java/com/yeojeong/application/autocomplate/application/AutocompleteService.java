package com.yeojeong.application.autocomplate.application;

import com.yeojeong.application.autocomplate.presentation.dto.AutocompleteResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AutocompleteService {

    private final RedisTemplate<String, String> redisTemplate;
    private final long limitCount;
    private final String suffix;
    private final String key;
    private final String scoreKey;

    public AutocompleteService(RedisTemplate<String, String> redisTemplate,
                               @Value("${autocomplete.limit}") long limitCount,
                               @Value("${autocomplete.suffix}") String suffix,
                               @Value("${autocomplete.key}") String key,
                               @Value("${autocomplete.score-key}") String scoreKey) {
        this.redisTemplate = redisTemplate;
        this.limitCount = limitCount;
        this.suffix = suffix;
        this.key = key;
        this.scoreKey = scoreKey;
    }

    public AutocompleteResponse getAutocomplete(String searchWord) {
        List<String> autocompleteList = getAutoCompleteListFromRedis(searchWord);
        return sortAutocompleteListByScore(autocompleteList);
    }

    public void addAutocomplete(String searchWord) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.incrementScore(scoreKey, searchWord, 1.0);

        if (zSetOperations.score(key, searchWord) == null) {
            for (int i = 1; i <= searchWord.length(); i++) {
                zSetOperations.add(key, searchWord.substring(0, i), 0.0);
            }
            zSetOperations.add(key, searchWord + suffix, 0.0);
        }
    }

    public List<String> getAutoCompleteListFromRedis(String searchWord) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        List<String> autocompleteList = new ArrayList<>();

        Long rank = zSetOperations.rank(key, searchWord);
        if (rank != null) {
            Set<String> rangeList = zSetOperations.range(key, rank, rank + 1000);
            if (rangeList != null) {
                autocompleteList = rangeList.stream()
                        .filter(value -> value.endsWith(suffix) && value.startsWith(searchWord))
                        .map(value -> StringUtils.removeEnd(value, suffix))
                        .limit(limitCount)
                        .collect(Collectors.toList());
            }
        }
        return autocompleteList;
    }

    public AutocompleteResponse sortAutocompleteListByScore(List<String> autocompleteList) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        List<AutocompleteResponse.Data> list = new ArrayList<>();

        for (String word : autocompleteList) {
            Double score = zSetOperations.score(scoreKey, word);
            if (score != null) {
                list.add(new AutocompleteResponse.Data(word, score));
            }
        }
        list.sort((a, b) -> Double.compare(b.score(), a.score()));
        return new AutocompleteResponse(list);
    }
}