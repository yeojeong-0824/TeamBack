package com.yeojeong.application.domain.board.board.application.imagefacade.implement;

import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.domain.board.board.application.imagefacade.ImageFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Service
public class ImageFacadeImpl implements ImageFacade {
    private final String[] checkExtensionArr = {"jpg"};

    @Value("${filePath}")
    private String PATH;

    @Override
    public String saveImage(MultipartFile image) {
        if (!checkExtension(image)) throw new RequestDataException("파일의 형식이 잘못되었습니다.");

        String originFileName = StringUtils.getFilename(image.getOriginalFilename());
        String serverFileName = UUID.randomUUID() + "_" + originFileName;

        String serverSavePath = PATH + serverFileName;
        try {
            File dest = new File(serverSavePath);
            image.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return serverFileName;
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
