package com.yeojeong.application.domain.utildto;

import com.yeojeong.application.config.util.BaseTime;
import lombok.Builder;

import java.time.LocalDateTime;

public class UtilResponse {
    @Builder
    public record TimeInfo(
            LocalDateTime createTime,
            LocalDateTime updateTime
    ){
        public static TimeInfo toDto(BaseTime baseTime){
            return TimeInfo.builder()
                    .createTime(baseTime.getCreateAt())
                    .updateTime(baseTime.getUpdateAt())
                    .build();
        }
    }
}
