package com.example.demo.dto.board;

import lombok.*;

@Data
@Builder
public class GoogleApiRequest {
    private String textQuery;
    private String languageCode;
}
