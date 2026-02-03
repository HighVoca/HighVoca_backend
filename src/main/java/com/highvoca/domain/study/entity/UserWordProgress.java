package com.highvoca.domain.study.entity;

import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.word.entity.Word;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
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
}