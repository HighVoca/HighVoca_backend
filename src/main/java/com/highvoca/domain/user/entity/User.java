package com.highvoca.domain.user.entity;

import com.highvoca.common.entity.BaseTimeEntity;
import com.highvoca.domain.user.enums.Role;
import com.highvoca.domain.user.enums.SocialProvider;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ROLE_USER'")
    private Role role;

    @Column(length = 500)
    private String refreshToken;

    @ColumnDefault("0")
    private Integer streakCnt;

    @ColumnDefault("100.0")
    private Double level;

    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    @ColumnDefault("25")
    private Integer dailyGoal;

    // --- 설정 (Settings) ---
    @ColumnDefault("true")
    private Boolean isProgressVisible;

    @ColumnDefault("true")
    private Boolean isLevelVisible;

    @ColumnDefault("true")
    private Boolean isNotificationEnabled;

    @ColumnDefault("'KO'")
    private String language;

    private LocalDateTime lastStudyDate;

    @Builder
    public User(String username, String email, SocialProvider provider, Role role) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.role = role;
    }
}