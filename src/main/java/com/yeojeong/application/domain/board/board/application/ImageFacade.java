package com.yeojeong.application.domain.board.board.application;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.domain.board.board.domain.Image;
import com.yeojeong.application.domain.board.board.presentation.dto.BoardResponse;
import com.yeojeong.application.domain.member.application.MemberService;
import com.yeojeong.application.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageFacade {

    private final MemberService memberService;
    private final ImageService imageService;
    private final String[] checkExtensionArr = {"jpg", "png"};

    @Value("${filePath}")
    private String PATH;
    
    public void delete(String id, Long memberId) {
        imageService.delete(id, memberId);
        String serverSavePath = PATH + id;
        File dest = new File(serverSavePath);

        if (dest.exists()) {
            boolean isDeleted = dest.delete();
            if (!isDeleted) {
                throw new RuntimeException("파일 삭제에 실패했습니다: " + serverSavePath);
            }
        } else {
            throw new NotFoundDataException("삭제할 파일이 존재하지 않습니다: " + serverSavePath);
        }

    }

    public BoardResponse.ImageUrl save(MultipartFile image, Long id) {
        if (!checkExtension(image)) throw new RequestDataException("파일의 확장자가 잘못되었습니다.");

        String originFileName = StringUtils.getFilename(image.getOriginalFilename());
        String serverFileName = UUID.randomUUID() + "_" + originFileName;

        String serverSavePath = PATH + serverFileName;
        try {
            File dest = new File(serverSavePath);
            image.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Member member = memberService.findById(id);
        Image entity = Image.builder()
                .url(serverFileName)
                .member(member)
                .build();
        imageService.save(entity);

        return BoardResponse.ImageUrl.toDto(serverFileName);
    }

    private boolean checkExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) return false;
        String extension = StringUtils.getFilenameExtension(filename);

        for (String checkExtension : checkExtensionArr) {
            if (extension != null && extension.equalsIgnoreCase(checkExtension)) {
                return true;
            }
        }
        return false;
    }
}
