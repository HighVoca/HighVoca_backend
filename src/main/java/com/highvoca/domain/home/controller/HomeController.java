package com.highvoca.domain.home.controller;

import com.highvoca.common.response.ApiResponse;
import com.highvoca.domain.home.dto.HomeDto.GoalUpdateRequest;
import com.highvoca.domain.home.dto.HomeDto.HomeResponse;
import com.highvoca.domain.home.service.HomeService;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import com.highvoca.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Home", description = "홈 화면 API")
@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Operation(summary = "홈 화면 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<HomeResponse>> getHome(HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        HomeResponse response = homeService.getHome(userId);
        return ResponseEntity.ok(ApiResponse.success("홈 화면 조회 성공", response));
    }

    @Operation(summary = "일일 목표 수정")
    @PatchMapping("/goal")
    public ResponseEntity<ApiResponse<Void>> updateGoal(HttpServletRequest httpRequest,
                                                        @RequestBody GoalUpdateRequest request) {
        Long userId = extractUserId(httpRequest);
        homeService.updateDailyGoal(userId, request);
        return ResponseEntity.ok(ApiResponse.success("일일 목표 수정 성공", null));
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
