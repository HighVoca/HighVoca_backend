package com.highvoca.domain.word.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WordStudyDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckAnswerRequest {
        private Long wordId;
        private String spelling;
        private String meaning;
        private String userInput;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CheckAnswerResponse {
        private Long wordId;
        private Boolean isCorrect;
        private String feedback;
    }
}
