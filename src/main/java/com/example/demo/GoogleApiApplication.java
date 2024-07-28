package com.example.demo;

import com.example.demo.dto.board.GoogleApiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GoogleApiApplication {
    public static void main(String[] args) {
        String url ="https://places.googleapis.com/v1/places:searchText";
        String key = "나만의 키 값";

        // 검색할 값
        String textQuery = "Spicy Vegetarian Food in Sydney, Australia";
        GoogleApiResponse result = GoogleApiResponse.builder()
                .textQuery(textQuery)
                .build();


        RestTemplate restTemplate = new RestTemplate();

        // 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-Goog-Api-Key", key);
        headers.set("X-Goog-FieldMask", "places.location,places.formattedAddress");  // 내가 받을 정보를 세팅

        // 보낼 바디와 헤더를 세팅
        HttpEntity<GoogleApiResponse> entity = new HttpEntity<>(result, headers);

        // post 로 요청
        ResponseEntity<String> responses = restTemplate.postForEntity(url, entity, String.class);

        System.out.println(responses.getBody());

        // TODO : 문자열을 다른 형태로 바꾸는 것이 필요

    }
}
