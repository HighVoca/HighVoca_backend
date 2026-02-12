package com.highvoca.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HomeDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class HomeResponse {
        private String nickname;
        private Integer streakCnt;
        private Integer dailyGoal;
        private Integer todayLearnedCount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoalUpdateRequest {
        private Integer dailyGoal;
    }
}
