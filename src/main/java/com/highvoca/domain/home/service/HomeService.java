package com.highvoca.domain.home.service;

import com.highvoca.domain.home.dto.HomeDto.GoalUpdateRequest;
import com.highvoca.domain.home.dto.HomeDto.HomeResponse;
import com.highvoca.domain.study.repository.UserWordProgressRepository;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserRepository userRepository;
    private final UserWordProgressRepository userWordProgressRepository;

    @Transactional(readOnly = true)
    public HomeResponse getHome(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        int todayLearnedCount = userWordProgressRepository
                .countByUserIdAndLastReviewedAtBetween(userId, startOfDay, endOfDay);

        return HomeResponse.builder()
                .nickname(user.getUsername())
                .streakCnt(user.getStreakCnt())
                .dailyGoal(user.getDailyGoal())
                .todayLearnedCount(todayLearnedCount)
                .build();
    }

    @Transactional
    public void updateDailyGoal(Long userId, GoalUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.updateDailyGoal(request.getDailyGoal());
    }
}
