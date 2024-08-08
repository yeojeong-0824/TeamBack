package com.example.demo.board.image.application;

import com.example.demo.board.image.domain.Image;
import com.example.demo.board.image.domain.ImageRepository;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.config.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public String updateImage(MultipartFile file) throws IOException {

        Image image = imageRepository.save(
                Image.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(ImageUtil.compressImage(file.getBytes()))
                        .build());

        if (image != null) {
            return "파일 업로드 성공 : " + file.getOriginalFilename();
        }
        return null;
    }

    @Override
    public byte[] downloadImage(String fileName) {

        Image image = imageRepository.findByName(fileName)
                .orElseThrow(() -> new NotFoundDataException("해당 이미지를 찾을 수 없습니다."));
        return ImageUtil.decompressImage(image.getImageData());
    }
}
