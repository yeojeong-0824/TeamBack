package com.yeojeong.application.domain.board.board.application.imagefacade;

import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageFacade {
    BoardResponse.ImageUrl saveImage(MultipartFile image);
}
