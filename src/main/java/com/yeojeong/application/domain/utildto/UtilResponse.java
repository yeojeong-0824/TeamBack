package com.yeojeong.application.domain.utildto;

import lombok.Builder;

import java.time.LocalDateTime;

public class UtilResponse {
    @Builder
    public record TimeInfo(
            LocalDateTime createTime,
            LocalDateTime updateTime
    ){}
}
