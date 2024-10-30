package com.yeojeong.application.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("emailAuthed")
public class EmailAuth {
    private String id;
    private String value;
    @TimeToLive
    private long ttl;

    public void authedEmail(String value) {
        this.value = value;
    }
}
