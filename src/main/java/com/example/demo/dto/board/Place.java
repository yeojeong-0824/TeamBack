package com.example.demo.dto.board;

import lombok.Data;

import java.util.List;

@Data
public class Place {
    private String formattedAddress;
    private Location location;
}
