package com.highvoca.domain.study.dto;

import com.highvoca.domain.word.entity.Word;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class VocabularyDto {

    @Getter
    @Builder
    public static class VocabularyResponse {
        private Long wordId;
        private String spelling;
        private String meaning;
        private String pronunciation;
        private Boolean isWrong;

        public static VocabularyResponse from(Word word, boolean isWrong) {
            return VocabularyResponse.builder()
                    .wordId(word.getId())
                    .spelling(word.getSpelling())
                    .meaning(word.getMeaning())
                    .pronunciation(word.getPronunciation())
                    .isWrong(isWrong)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class VocabularyPageResponse {
        private List<VocabularyResponse> content;
        private PageInfo pageInfo;
    }

    @Getter
    @Builder
    public static class PageInfo {
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
    }
}