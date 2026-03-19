package com.highvoca.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthDto {

    // 일회용 코드 교환 요청
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CodeExchangeRequest {
        private String code;
    }

    // 토큰 재발급 요청
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueRequest {
        private String refreshToken;
    }

    // 토큰 응답 (공통)
    @Getter
    @AllArgsConstructor
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
    }
}