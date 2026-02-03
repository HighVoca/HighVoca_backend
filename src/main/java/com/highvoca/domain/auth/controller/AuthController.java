package com.highvoca.domain.auth.controller;

import com.highvoca.domain.auth.dto.AuthDto;
import com.highvoca.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody AuthDto.ReissueRequest request) {
        AuthDto.TokenResponse tokens = authService.reissue(request);

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "COMMON200",
                "message", "토큰 재발급 성공",
                "result", tokens
        ));
    }
}