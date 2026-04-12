package com.highvoca.domain.word.repository;

import com.highvoca.domain.word.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(value = "SELECT * FROM word WHERE level = :level ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Word> findRandomWordsByLevel(@Param("level") int level, @Param("count") int count);

    @Query(value = "SELECT * FROM word WHERE level BETWEEN :minLevel AND :maxLevel ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Word> findRandomWordsByLevelRange(@Param("minLevel") int minLevel, @Param("maxLevel") int maxLevel, @Param("limit") int limit);

    @Query(value = "SELECT * FROM word w WHERE w.level BETWEEN :minLevel AND :maxLevel " +
            "AND w.word_id NOT IN (SELECT uwp.word_id FROM user_word_progress uwp WHERE uwp.user_id = :userId) " +
            "ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Word> findNewWordsForUser(@Param("userId") Long userId, @Param("minLevel") int minLevel,
                                   @Param("maxLevel") int maxLevel, @Param("limit") int limit);

    @Query(value = "SELECT * FROM word w WHERE w.word_id NOT IN (:excludeIds) ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Word> findRandomWordsExcluding(@Param("excludeIds") List<Long> excludeIds, @Param("limit") int limit);

    Page<Word> findByLevel(Integer level, Pageable pageable);
}