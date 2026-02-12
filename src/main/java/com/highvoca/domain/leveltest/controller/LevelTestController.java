package com.highvoca.domain.leveltest.controller;

import com.highvoca.common.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<StepResponse>> step1(@RequestBody Step1Request request) {
        StepResponse response = levelTestService.getStep1Words(request);
        return ResponseEntity.ok(ApiResponse.success("Step 1 단어 조회 성공", response));
    }

    @Operation(summary = "Step 2")
    @PostMapping("/step2")
    public ResponseEntity<ApiResponse<StepResponse>> step2(@RequestBody StepRequest request) {
        StepResponse response = levelTestService.getNextStepWords(request);
        return ResponseEntity.ok(ApiResponse.success("Step 2 단어 조회 성공", response));
    }

    @Operation(summary = "Step 3")
    @PostMapping("/step3")
    public ResponseEntity<ApiResponse<StepResponse>> step3(@RequestBody StepRequest request) {
        StepResponse response = levelTestService.getNextStepWords(request);
        return ResponseEntity.ok(ApiResponse.success("Step 3 단어 조회 성공", response));
    }

    @Operation(summary = "Step 4")
    @PostMapping("/step4")
    public ResponseEntity<ApiResponse<StepResponse>> step4(@RequestBody StepRequest request) {
        StepResponse response = levelTestService.getNextStepWords(request);
        return ResponseEntity.ok(ApiResponse.success("Step 4 단어 조회 성공", response));
    }

    @Operation(summary = "최종 제출 - AI 분석 및 레벨 확정")
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<SubmitResponse>> submit(HttpServletRequest httpRequest,
                                                              @RequestBody SubmitRequest request) {
        String token = jwtTokenProvider.resolveToken(httpRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("AUTH401", "유효하지 않은 토큰입니다."));
        }

        String email = jwtTokenProvider.getEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        SubmitResponse response = levelTestService.submitAndAnalyze(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("레벨 테스트 완료", response));
    }
}
