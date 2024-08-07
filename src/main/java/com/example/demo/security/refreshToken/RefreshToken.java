package com.example.demo.security.refreshToken;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

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
