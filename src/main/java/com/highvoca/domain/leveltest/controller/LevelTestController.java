package com.highvoca.domain.leveltest.controller;

import com.highvoca.domain.leveltest.dto.LevelTestDto.*;
import com.highvoca.domain.leveltest.service.LevelTestService;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import com.highvoca.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Level Test", description = "레벨 테스트 API")
@RestController
@RequestMapping("/api/v1/level-test")
@RequiredArgsConstructor
public class LevelTestController {

    private final LevelTestService levelTestService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Operation(summary = "Step 1 - 초기 난이도 선택")
    @PostMapping("/step1")
    public ResponseEntity<?> step1(@RequestBody Step1Request request) {
        StepResponse response = levelTestService.getStep1Words(request);

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "COMMON200",
                "message", "Step 1 단어 조회 성공",
                "result", response
        ));
    }

    @Operation(summary = "Step 2 - 적응형 단어 조회")
    @PostMapping("/step2")
    public ResponseEntity<?> step2(@RequestBody StepRequest request) {
        StepResponse response = levelTestService.getNextStepWords(request);

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "COMMON200",
                "message", "Step 2 단어 조회 성공",
                "result", response
        ));
    }

    @Operation(summary = "Step 3 - 적응형 단어 조회")
    @PostMapping("/step3")
    public ResponseEntity<?> step3(@RequestBody StepRequest request) {
        StepResponse response = levelTestService.getNextStepWords(request);

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "COMMON200",
                "message", "Step 3 단어 조회 성공",
                "result", response
        ));
    }

    @Operation(summary = "Step 4 - 적응형 단어 조회")
    @PostMapping("/step4")
    public ResponseEntity<?> step4(@RequestBody StepRequest request) {
        StepResponse response = levelTestService.getNextStepWords(request);

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "COMMON200",
                "message", "Step 4 단어 조회 성공",
                "result", response
        ));
    }

    @Operation(summary = "최종 제출 - AI 분석 및 레벨 확정")
    @PostMapping("/submit")
    public ResponseEntity<?> submit(HttpServletRequest httpRequest,
                                    @RequestBody SubmitRequest request) {
        String token = jwtTokenProvider.resolveToken(httpRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).body(Map.of(
                    "isSuccess", false,
                    "code", "AUTH401",
                    "message", "유효하지 않은 토큰입니다.",
                    "result", Map.of()
            ));
        }

        String email = jwtTokenProvider.getEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        SubmitResponse response = levelTestService.submitAndAnalyze(user.getId(), request);

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "COMMON200",
                "message", "레벨 테스트 완료",
                "result", response
        ));
    }
}
