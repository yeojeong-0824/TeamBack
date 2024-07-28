package com.example.demo.security.refreshToken;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@Builder @AllArgsConstructor @NoArgsConstructor
public class RefreshToken {
    @Id
    private String refreshToken;
    private String username;
    private String nickname;
    private Integer age;
    private String role;
    private Long expirationTime;
    private Integer count;

    public void counting() {
        this.count--;
    }
}
