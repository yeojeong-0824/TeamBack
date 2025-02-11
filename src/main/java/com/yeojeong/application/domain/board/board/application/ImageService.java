package com.yeojeong.application.domain.board.board.application;

import com.yeojeong.application.config.exception.OwnershipException;
import com.yeojeong.application.domain.board.board.domain.Image;
import com.yeojeong.application.domain.board.board.domain.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public void save(Image image) {
        imageRepository.save(image);
    }

    public void delete(Image image, Long id) {
        if(!id.equals(image.getMember().getId())) throw new OwnershipException("이미지를 생성한 회원이 아닙니다.");
        imageRepository.delete(image);
    }
}
