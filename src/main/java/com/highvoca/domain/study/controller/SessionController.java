package com.highvoca.domain.study.controller;

import com.highvoca.common.response.ApiResponse;
import com.highvoca.domain.study.dto.SessionDto.FinishResponse;
import com.highvoca.domain.study.dto.SessionDto.SessionRequest;
import com.highvoca.domain.study.service.SessionService;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import com.highvoca.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Study Session", description = "학습 세션 API")
@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Operation(summary = "학습 세션 완료")
    @PostMapping
    public ResponseEntity<ApiResponse<FinishResponse>> finishSession(
            HttpServletRequest httpRequest,
            @RequestBody SessionRequest request) {
        Long userId = extractUserId(httpRequest);
        FinishResponse response = sessionService.finishSession(userId, request);
        return ResponseEntity.ok(ApiResponse.success("학습 세션 완료", response));
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
