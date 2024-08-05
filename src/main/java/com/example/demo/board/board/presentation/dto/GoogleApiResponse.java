package com.example.demo.board.board.presentation.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleApiResponse {
    private List<Place> places;

    @Data
    public static class Place {
        private DisplayName displayName;
        private String formattedAddress;
        private Location location;
    }

    @Data
    public static class Location {
        private Float latitude;
        private Float longitude;
    }

    @Data
    public static class DisplayName {
        private String text;
        private String languageCode;
    }
}
