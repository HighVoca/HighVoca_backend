package com.highvoca.domain.study.entity;

import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.word.entity.Word;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_word_progress")
public class UserWordProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Column(name = "current_stage")
    private Integer currentStage;

    @Column(name = "last_reviewed_at")
    private LocalDateTime lastReviewedAt;

    @Column(name = "next_review_at")
    private LocalDateTime nextReviewAt;

    public void incrementStage() {
        this.currentStage++;
        this.lastReviewedAt = LocalDateTime.now();
        this.nextReviewAt = calculateNextReview(this.currentStage);
    }

    public void resetStage() {
        this.currentStage = 0;
        this.lastReviewedAt = LocalDateTime.now();
        this.nextReviewAt = LocalDateTime.now();
    }

    private LocalDateTime calculateNextReview(int stage) {
        // SRS intervals: 1h, 1d, 3d, 7d, 14d, 30d, 60d
        long[] hoursInterval = {1, 24, 72, 168, 336, 720, 1440};
        int index = Math.min(stage - 1, hoursInterval.length - 1);
        long hours = hoursInterval[Math.max(index, 0)];
        return LocalDateTime.now().plusHours(hours);
    }
}