package com.highvoca.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    @Getter
    @Builder
    public static class MyPageResponse {
        private Long userId;
        private String username;
        private String email;
        private String provider;
        private Integer streakCnt;
        private Double level;
        private StatsDto stats;
        private SettingsDto settings;
    }

    @Getter
    @Builder
    public static class StatsDto {
        private Integer totalWords;
        private Double averageAccuracy;
        private Integer totalStudyDays;
    }

    @Getter
    @Builder
    public static class SettingsDto {
        private Boolean isProgressVisible;
        private Boolean isLevelVisible;
        private Boolean isNotificationEnabled;
        private String language;
    }

    @Getter
    @NoArgsConstructor
    public static class ProgressSettingRequest {
        private Boolean isProgressVisible;
    }

    @Getter
    @NoArgsConstructor
    public static class LevelSettingRequest {
        private Boolean isLevelVisible;
    }
}