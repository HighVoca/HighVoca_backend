package com.highvoca.domain.study.repository;

import com.highvoca.domain.study.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    @Query("SELECT COALESCE(AVG(s.accuracy), 0.0) FROM StudySession s WHERE s.user.id = :userId")
    Double findAverageAccuracyByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT COUNT(DISTINCT DATE(created_at)) FROM study_session WHERE user_id = :userId", nativeQuery = true)
    Integer countDistinctStudyDaysByUserId(@Param("userId") Long userId);
}