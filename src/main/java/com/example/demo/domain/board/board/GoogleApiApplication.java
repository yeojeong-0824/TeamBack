package com.example.demo.domain.board.board;

import com.example.demo.domain.board.board.presentation.dto.GoogleApiRequest;
import com.example.demo.domain.board.board.presentation.dto.GoogleApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GoogleApiApplication {

    // 메소드 명만 수정했습니다!
    public void ggogleApi() {
        String url ="https://places.googleapis.com/v1/places:searchText";
        String key = "나만의 키 값";

        // 검색할 값
        String textQuery = "남산타워";
        GoogleApiRequest result = GoogleApiRequest.builder()
                .textQuery(textQuery)
                .languageCode("ko")
                .build();

        RestTemplate restTemplate = new RestTemplate();

        // 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-Goog-Api-Key", key);
        headers.set("X-Goog-FieldMask", "places.location,places.formattedAddress");  // 내가 받을 정보를 세팅

        // 보낼 바디와 헤더를 세팅
        HttpEntity<GoogleApiRequest> entity = new HttpEntity<>(result, headers);

        // post 로 요청
        ResponseEntity<String> responses = restTemplate.postForEntity(url, entity, String.class);

        // 문자열을 dto 로 변환
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            GoogleApiResponse result1 = objectMapper.readValue(responses.getBody(), GoogleApiResponse.class);

            // 확인용
            /*for(Place place: result1.getPlaces()){
                System.out.println(place.getFormattedAddress());
            }*/
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }
}
