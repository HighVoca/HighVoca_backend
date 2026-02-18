package com.highvoca.domain.study.repository;

import com.highvoca.domain.study.entity.UserWordProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserWordProgressRepository extends JpaRepository<UserWordProgress, Long> {
    Optional<UserWordProgress> findByUserIdAndWordId(Long userId, Long wordId);

    int countByUserIdAndLastReviewedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT uwp FROM UserWordProgress uwp JOIN FETCH uwp.word " +
           "WHERE uwp.user.id = :userId AND uwp.nextReviewAt <= :now " +
           "ORDER BY uwp.nextReviewAt ASC")
    List<UserWordProgress> findReviewableWords(@Param("userId") Long userId, @Param("now") LocalDateTime now);
}
