package com.yeojeong.application.config.webconfig;

import com.yeojeong.application.config.exception.NotFoundDataException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageResourceResolver implements ResourceResolver {

    private final String PATH;

    public ImageResourceResolver(String PATH) {
        this.PATH = PATH;
    }

    @Override
    public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        File file = new File(PATH + requestPath);
        if(!file.exists()) throw new NotFoundDataException("파일을 찾을 수 없습니다: " + requestPath);
        try {
            return new UrlResource(file.toURI());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return chain.resolveUrlPath(resourcePath, locations);
    }
}
