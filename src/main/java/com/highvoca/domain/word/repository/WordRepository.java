package com.highvoca.domain.word.repository;

import com.highvoca.domain.word.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(value = "SELECT * FROM Word WHERE level = :level ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Word> findRandomWordsByLevel(@Param("level") int level, @Param("count") int count);
}
