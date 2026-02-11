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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@EntityListeners(AuditingEntityListener.class) // created_at, updated_at 자동 관리
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

    @Column(name = "streak_cnt")
    private Integer streakCnt;

    private Double level;

    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    @Column(name = "refresh_token", length = 500)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "last_study_date")
    private LocalDateTime lastStudyDate;

    @Column(name = "daily_goal")
    private Integer dailyGoal;

    @Column(name = "is_progress_visible")
    private Boolean isProgressVisible;

    @Column(name = "is_notification_enabled")
    private Boolean isNotificationEnabled;

    @Column(name = "is_level_visible")
    private Boolean isLevelVisible;

    @Column(length = 10)
    private String language;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public User(String username, String email, SocialProvider provider, Role role) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.role = role;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateLevel(Double level) {
        this.level = level;
    }
}