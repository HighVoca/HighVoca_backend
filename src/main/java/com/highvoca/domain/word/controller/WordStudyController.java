package com.highvoca.domain.word.controller;

import com.highvoca.common.response.ApiResponse;
import com.highvoca.domain.word.dto.WordStudyDto.CheckAnswerRequest;
import com.highvoca.domain.word.dto.WordStudyDto.CheckAnswerResponse;
import com.highvoca.domain.word.service.AiWordCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Word Study", description = "단어 학습 API")
@RestController
@RequestMapping("/api/v1/words")
@RequiredArgsConstructor
public class WordStudyController {

    private final AiWordCheckService aiWordCheckService;

    @Operation(summary = "답안 제출 및 AI 채점")
    @PostMapping("/check")
    public ResponseEntity<ApiResponse<CheckAnswerResponse>> checkAnswer(@RequestBody CheckAnswerRequest request) {
        CheckAnswerResponse response = aiWordCheckService.check(request);
        return ResponseEntity.ok(ApiResponse.success("채점 완료", response));
    }
}
