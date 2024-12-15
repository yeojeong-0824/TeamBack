package com.yeojeong.application.security.config.refreshtoken.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder @AllArgsConstructor @NoArgsConstructor
@RedisHash("refreshToken")
public class RefreshToken {
    @Id
    private String id;
    private String key;
    @TimeToLive
    private long ttl;
}
