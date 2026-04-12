package com.highvoca.domain.auth.controller;

import com.highvoca.common.response.ApiResponse;
import com.highvoca.domain.auth.dto.AuthDto;
import com.highvoca.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API", description = "인증 및 토큰 관리 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인 (토큰 발급)", description = "프론트엔드에서 전달받은 일회용 인가 코드를 검증하고 JWT 액세스 및 리프레시 토큰을 발급합니다.")
    @PostMapping("/code")
    public ResponseEntity<ApiResponse<AuthDto.TokenResponse>> exchangeCode(
            @RequestBody AuthDto.CodeExchangeRequest request) {
        AuthDto.TokenResponse tokens = authService.exchangeCode(request);
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", tokens));
    }

    @Operation(summary = "토큰 재발급", description = "유효한 리프레시 토큰을 사용하여 새로운 액세스 토큰을 재발급합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<AuthDto.TokenResponse>> reissue(
            @RequestBody AuthDto.ReissueRequest request) {
        AuthDto.TokenResponse tokens = authService.reissue(request);
        return ResponseEntity.ok(ApiResponse.success("토큰 재발급 성공", tokens));
    }
}