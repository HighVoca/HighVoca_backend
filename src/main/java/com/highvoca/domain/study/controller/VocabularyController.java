package com.highvoca.domain.study.controller;

import com.highvoca.domain.study.dto.VocabularyDto;
import com.highvoca.domain.study.service.VocabularyService;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import com.highvoca.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Vocabulary API", description = "단어장 조회 API")
@RestController
@RequestMapping("/api/v1/vocabularies")
@RequiredArgsConstructor
public class VocabularyController {

    private final VocabularyService vocabularyService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Operation(summary = "날짜별 단어장 조회", description = "특정 날짜에 학습한 단어(LEARNED) 또는 틀린 단어(WRONG) 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getVocabularies(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String filter) {

        Long userId = getUserIdFromRequest(request);
        List<VocabularyDto.VocabularyResponse> result = vocabularyService.getVocabularies(userId, date, filter);

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "COMMON200",
                "message", "단어장 조회 성공",
                "result", result
        ));
    }

    @Operation(summary = "모든 단어 보기 (페이징)", description = "선택한 레벨의 모든 단어를 페이징하여 조회하며, 틀린 이력이 있는 단어를 표시합니다.")
    @GetMapping("/all")
    public ResponseEntity<?> getAllVocabularies(
            HttpServletRequest request,
            @RequestParam Integer level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Long userId = getUserIdFromRequest(request);
        VocabularyDto.VocabularyPageResponse result = vocabularyService.getAllVocabularies(userId, level, page, size);

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "COMMON200",
                "message", "모든 단어 조회 성공",
                "result", result
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