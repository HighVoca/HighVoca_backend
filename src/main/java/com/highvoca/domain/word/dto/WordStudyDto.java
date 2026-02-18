package com.highvoca.domain.word.dto;

import com.highvoca.domain.word.entity.Word;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WordStudyDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class WordStudyResponse {
        private Long wordId;
        private String spelling;
        private String meaning;
        private Integer level;
        private String pronunciation;
        private String partOfSpeech;
        private String audioUrl;
        private Boolean isReview;

        public static WordStudyResponse from(Word word, boolean isReview) {
            return WordStudyResponse.builder()
                    .wordId(word.getId())
                    .spelling(word.getSpelling())
                    .meaning(word.getMeaning())
                    .level(word.getLevel())
                    .pronunciation(word.getPronunciation())
                    .partOfSpeech(word.getPartOfSpeech())
                    .audioUrl(word.getAudioUrl())
                    .isReview(isReview)
                    .build();
        }
    }

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
