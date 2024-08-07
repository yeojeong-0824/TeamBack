package com.example.demo.autocomplate.controller.dto;

import java.util.List;

public record AutocompleteResponse(List<Data> list) {

    public record Data(String value, double score) {
    }
}

