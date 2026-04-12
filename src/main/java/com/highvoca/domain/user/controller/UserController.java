package com.highvoca.domain.user.controller;

import com.highvoca.domain.user.dto.UserDto;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import com.highvoca.domain.user.service.UserService;
import com.highvoca.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "User API", description = "유저 마이페이지 및 설정 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Operation(summary = "마이페이지 조회", description = "내 정보와 학습 통계(총 학습 단어, 평균 정답률 등)를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<?> getMyPage(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        UserDto.MyPageResponse result = userService.getMyPageInfo(userId);

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "200",
                "message", "마이페이지 조회 성공",
                "result", result
        ));
    }

    @Operation(summary = "진행도 표시 설정 변경", description = "학습 중 진행도 표시 여부(true/false)를 변경합니다.")
    @PatchMapping("/settings/progress")
    public ResponseEntity<?> updateProgressSetting(
            HttpServletRequest request,
            @RequestBody UserDto.ProgressSettingRequest settingRequest) {

        Long userId = getUserIdFromRequest(request);
        userService.updateProgressVisible(userId, settingRequest.getIsProgressVisible());

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "200",
                "message", "진행도 표시 설정이 변경되었습니다."
        ));
    }

    @Operation(summary = "단어 레벨 표시 설정 변경", description = "단어 레벨 표시 여부(true/false)를 변경합니다.")
    @PatchMapping("/settings/level")
    public ResponseEntity<?> updateLevelSetting(
            HttpServletRequest request,
            @RequestBody UserDto.LevelSettingRequest settingRequest) {

        Long userId = getUserIdFromRequest(request);
        userService.updateLevelVisible(userId, settingRequest.getIsLevelVisible());

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "200",
                "message", "단어 레벨 표시 설정이 변경되었습니다."
        ));
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new RuntimeException("토큰이 존재하지 않거나 유효하지 않습니다.");
        }
        String email = jwtTokenProvider.getEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저를 찾을 수 없습니다."));
        return user.getId();
    }
}