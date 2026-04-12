package com.highvoca.domain.study.repository;

import com.highvoca.domain.study.entity.WrongWordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface WrongWordHistoryRepository extends JpaRepository<WrongWordHistory, Long> {

    List<WrongWordHistory> findByUserIdAndWrongDate(Long userId, LocalDate wrongDate);

    @Query("SELECT w.word.id FROM WrongWordHistory w WHERE w.user.id = :userId AND w.word.id IN :wordIds")
    Set<Long> findWrongWordIdsByUserIdAndWordIdIn(@Param("userId") Long userId, @Param("wordIds") List<Long> wordIds);
}