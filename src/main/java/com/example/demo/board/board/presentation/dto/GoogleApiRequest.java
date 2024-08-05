package com.example.demo.board.board.presentation.dto;

import lombok.*;

@Data
@Builder
public class GoogleApiRequest {
    private String textQuery;
    private String includedType;
    private String languageCode;
}
