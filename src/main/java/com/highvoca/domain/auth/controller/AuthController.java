package com.highvoca.domain.auth.controller;

import com.highvoca.common.response.ApiResponse;
import com.highvoca.domain.auth.dto.AuthDto;
import com.highvoca.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 일회용 코드 → 토큰 교환
    @PostMapping("/code")
    public ResponseEntity<ApiResponse<AuthDto.TokenResponse>> exchangeCode(
            @RequestBody AuthDto.CodeExchangeRequest request) {
        AuthDto.TokenResponse tokens = authService.exchangeCode(request);
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", tokens));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<AuthDto.TokenResponse>> reissue(
            @RequestBody AuthDto.ReissueRequest request) {
        AuthDto.TokenResponse tokens = authService.reissue(request);
        return ResponseEntity.ok(ApiResponse.success("토큰 재발급 성공", tokens));
    }
}