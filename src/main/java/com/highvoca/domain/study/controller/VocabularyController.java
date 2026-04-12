package com.highvoca.domain.study.controller;

import com.highvoca.domain.study.dto.VocabularyDto;
import com.highvoca.domain.study.service.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vocabularies")
@RequiredArgsConstructor
public class VocabularyController {

    private final VocabularyService vocabularyService;

    // 1. 날짜별 단어장 조회 (학습한 단어 / 틀린 단어)
    @GetMapping
    public ResponseEntity<?> getVocabularies(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String filter) {

        Long userId = Long.parseLong(authentication.getName());
        List<VocabularyDto.VocabularyResponse> result = vocabularyService.getVocabularies(userId, date, filter);

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "COMMON200",
                "message", "단어장 조회 성공",
                "result", result
        ));
    }

    // 2. 모든 단어 보기 (레벨별 페이징)
    @GetMapping("/all")
    public ResponseEntity<?> getAllVocabularies(
            Authentication authentication,
            @RequestParam Integer level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Long userId = Long.parseLong(authentication.getName());
        VocabularyDto.VocabularyPageResponse result = vocabularyService.getAllVocabularies(userId, level, page, size);

        return ResponseEntity.ok(Map.of(
                "isSuccess", true,
                "code", "COMMON200",
                "message", "모든 단어 조회 성공",
                "result", result
        ));
    }
}