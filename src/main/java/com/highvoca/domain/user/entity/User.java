package com.highvoca.domain.user.entity;

import com.highvoca.domain.user.enums.Role;
import com.highvoca.domain.user.enums.SocialProvider;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Builder.Default
    @Column(name = "streak_cnt")
    private Integer streakCnt = 0;

    @Builder.Default
    private Double level = 1.0;

    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    @Column(name = "refresh_token", length = 500)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "last_study_date")
    private LocalDateTime lastStudyDate;

    @Builder.Default
    @Column(name = "daily_goal")
    private Integer dailyGoal = 25;

    @Builder.Default
    @Column(name = "is_progress_visible")
    private Boolean isProgressVisible = true;

    @Builder.Default
    @Column(name = "is_notification_enabled")
    private Boolean isNotificationEnabled = false;

    @Builder.Default
    @Column(name = "is_level_visible")
    private Boolean isLevelVisible = true;

    @Builder.Default
    @Column(length = 10)
    private String language = "KO";

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateLevel(Double level) {
        this.level = level;
    }

    public void updateDailyGoal(Integer dailyGoal) {
        this.dailyGoal = dailyGoal;
    }
}