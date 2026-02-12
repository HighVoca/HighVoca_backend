package com.highvoca.domain.leveltest.dto;

import com.highvoca.domain.word.entity.Word;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class LevelTestDto {

    // ===== Enums =====

    public enum Difficulty {
        BEGINNER(1, 5),
        INTERMEDIATE(6, 10),
        ADVANCED(11, 15),
        EXPERT(16, 20);

        @Getter
        private final int startLevel;
        @Getter
        private final int endLevel;

        Difficulty(int startLevel, int endLevel) {
            this.startLevel = startLevel;
            this.endLevel = endLevel;
        }
    }

    // ===== Request DTOs =====

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Step1Request {
        private Difficulty difficulty;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StepRequest {
        private List<QuestionResult> results;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubmitRequest {
        private List<QuestionResult> totalResult;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionResult {
        private Long wordId;
        private Boolean isCorrect;
    }

    // ===== Response DTOs =====

    @Getter
    @Builder
    @AllArgsConstructor
    public static class StepResponse {
        private List<WordDto> words;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class WordDto {
        private Long wordId;
        private String spelling;
        private String meaning;
        private Integer level;
        private String pronunciation;
        private String partOfSpeech;

        public static WordDto from(Word word) {
            return WordDto.builder()
                    .wordId(word.getId())
                    .spelling(word.getSpelling())
                    .meaning(word.getMeaning())
                    .level(word.getLevel())
                    .pronunciation(word.getPronunciation())
                    .partOfSpeech(word.getPartOfSpeech())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SubmitResponse {
        private Double finalLevel;
        private String percentile;
        private String levelName;
        private String aiAnalysis;
    }
}
