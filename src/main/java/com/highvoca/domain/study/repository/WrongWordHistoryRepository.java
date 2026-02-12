package com.highvoca.domain.study.repository;

import com.highvoca.domain.study.entity.WrongWordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WrongWordHistoryRepository extends JpaRepository<WrongWordHistory, Long> {
}
