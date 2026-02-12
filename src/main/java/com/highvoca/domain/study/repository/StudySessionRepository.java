package com.highvoca.domain.study.repository;

import com.highvoca.domain.study.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
}
