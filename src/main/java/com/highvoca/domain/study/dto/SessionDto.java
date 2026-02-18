package com.highvoca.domain.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SessionDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionRequest {
        private List<SessionWordResult> results;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionWordResult {
        private Long wordId;
        private Boolean isCorrect;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FinishResponse {
        private Long sessionId;
        private Integer streakCnt;
        private Integer score;
        private Double accuracy;
    }
}
