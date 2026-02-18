package com.highvoca.domain.word.controller;

import com.highvoca.common.response.ApiResponse;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import com.highvoca.domain.word.dto.WordStudyDto.CheckAnswerRequest;
import com.highvoca.domain.word.dto.WordStudyDto.CheckAnswerResponse;
import com.highvoca.domain.word.dto.WordStudyDto.WordStudyResponse;
import com.highvoca.domain.word.service.AiWordCheckService;
import com.highvoca.domain.word.service.WordStudyService;
import com.highvoca.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Word Study", description = "단어 학습 API")
@RestController
@RequestMapping("/api/v1/words")
@RequiredArgsConstructor
public class WordStudyController {

    private final AiWordCheckService aiWordCheckService;
    private final WordStudyService wordStudyService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Operation(summary = "오늘의 학습 단어 조회")
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<WordStudyResponse>>> getTodayWords(HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        List<WordStudyResponse> response = wordStudyService.getTodayWords(userId);
        return ResponseEntity.ok(ApiResponse.success("오늘의 학습 단어 조회 성공", response));
    }

    @Operation(summary = "답안 제출 및 AI 채점")
    @PostMapping("/check")
    public ResponseEntity<ApiResponse<CheckAnswerResponse>> checkAnswer(@RequestBody CheckAnswerRequest request) {
        CheckAnswerResponse response = aiWordCheckService.check(request);
        return ResponseEntity.ok(ApiResponse.success("채점 완료", response));
    }

    private Long extractUserId(HttpServletRequest httpRequest) {
        String token = jwtTokenProvider.resolveToken(httpRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        String email = jwtTokenProvider.getEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return user.getId();
    }
}
