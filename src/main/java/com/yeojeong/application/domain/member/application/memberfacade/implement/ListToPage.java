package com.yeojeong.application.domain.member.application.memberfacade.implement;

import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.presentation.dto.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListToPage<T> {

    private final int pageSize = 10;

    public Page<T> toPage(List<T> list, int page) {
        if(list.isEmpty()) return new PageImpl<>(new ArrayList<>());

        if (page < 0) {
            throw new IllegalArgumentException("Page number must be 0 or greater");
        }

        int totalSize = list.size();
        int totalPages = (int) Math.ceil((double) totalSize / pageSize);

        if (page >= totalPages) {
            page = totalPages - 1;
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        List<T> pageContent = new ArrayList<>(list.subList(start, end));
        return new PageImpl<>(pageContent, pageable, totalSize);
    }
}
