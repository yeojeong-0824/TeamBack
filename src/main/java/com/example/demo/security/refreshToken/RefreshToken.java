package com.example.demo.security.refreshToken;

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
    private Long expirationTime;
    private Integer count;
    @TimeToLive
    private long ttl;

    public void counting() {
        this.count--;
    }
}
