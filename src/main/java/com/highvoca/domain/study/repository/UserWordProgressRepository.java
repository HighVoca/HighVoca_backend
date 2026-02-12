package com.highvoca.domain.study.repository;

import com.highvoca.domain.study.entity.UserWordProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserWordProgressRepository extends JpaRepository<UserWordProgress, Long> {
    Optional<UserWordProgress> findByUserIdAndWordId(Long userId, Long wordId);

    int countByUserIdAndLastReviewedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
