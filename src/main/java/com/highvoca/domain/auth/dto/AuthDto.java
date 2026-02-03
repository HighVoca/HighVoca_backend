package com.highvoca.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthDto {

    // 토큰 재발급 요청 (프론트가 보낼 데이터)
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueRequest {
        private String refreshToken;
    }

    // 토큰 응답 (서버가 줄 데이터)
    @Getter
    @AllArgsConstructor
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
    }
}