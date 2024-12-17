package com.yeojeong.application.domain.board.board.application.imagefacade;

import org.springframework.web.multipart.MultipartFile;

public interface ImageFacade {
    String saveImage(MultipartFile image);
}
