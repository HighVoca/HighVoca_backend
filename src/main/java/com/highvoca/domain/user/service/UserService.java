package com.highvoca.domain.user.service;

import com.highvoca.domain.study.repository.StudySessionRepository;
import com.highvoca.domain.study.repository.UserWordProgressRepository;
import com.highvoca.domain.user.dto.UserDto;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserWordProgressRepository userWordProgressRepository;
    private final StudySessionRepository studySessionRepository;

    // 마이페이지 데이터 조립
    public UserDto.MyPageResponse getMyPageInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 통계 계산
        int totalWords = userWordProgressRepository.countByUserId(userId);
        Double avgAccuracy = studySessionRepository.findAverageAccuracyByUserId(userId);
        Integer totalStudyDays = studySessionRepository.countDistinctStudyDaysByUserId(userId);

        // 소수점 첫째 자리까지만 표시 (예: 87.04 -> 87.0)
        double roundedAccuracy = Math.round(avgAccuracy * 10) / 10.0;

        return UserDto.MyPageResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .provider(user.getProvider().name())
                .streakCnt(user.getStreakCnt())
                .level(user.getLevel())
                .stats(UserDto.StatsDto.builder()
                        .totalWords(totalWords)
                        .averageAccuracy(roundedAccuracy)
                        .totalStudyDays(totalStudyDays)
                        .build())
                .settings(UserDto.SettingsDto.builder()
                        .isProgressVisible(user.getIsProgressVisible())
                        .isLevelVisible(user.getIsLevelVisible())
                        .isNotificationEnabled(user.getIsNotificationEnabled())
                        .language(user.getLanguage())
                        .build())
                .build();
    }

    @Transactional
    public void updateProgressVisible(Long userId, Boolean isVisible) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        user.updateProgressVisible(isVisible);
    }

    @Transactional
    public void updateLevelVisible(Long userId, Boolean isVisible) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        user.updateLevelVisible(isVisible);
    }
}