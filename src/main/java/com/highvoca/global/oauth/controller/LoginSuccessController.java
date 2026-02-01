package com.highvoca.global.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginSuccessController {

    @GetMapping("/login-success")
    public Map<String, String> loginSuccess(@RequestParam("accessToken") String accessToken,
                                            @RequestParam("refreshToken") String refreshToken) {
        return Map.of(
                "message", "축하합니다! 카카오 로그인에 성공했습니다.",
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }
}