package com.highvoca.domain.word.repository;

import com.highvoca.domain.word.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(value = "SELECT * FROM word WHERE level = :level ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Word> findRandomWordsByLevel(@Param("level") int level, @Param("count") int count);

    @Query(value = "SELECT * FROM word WHERE level BETWEEN :minLevel AND :maxLevel ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Word> findRandomWordsByLevelRange(@Param("minLevel") int minLevel, @Param("maxLevel") int maxLevel, @Param("limit") int limit);
}
