package com.example.demo.dto.autocomplete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AutocompleteResponse {

    private final List<Data> list;

    @Getter
    @AllArgsConstructor
    public static class Data {
        private final String value;
        private final double score;
    }
}

