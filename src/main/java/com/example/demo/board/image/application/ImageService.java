package com.example.demo.board.image.application;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String uploadImage(MultipartFile file) throws IOException;

    byte[] downloadImage(String fileName);
}
