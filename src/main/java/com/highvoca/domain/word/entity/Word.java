package com.highvoca.domain.word.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Word")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id") // SQL의 word_id와 매핑
    private Long id;

    @Column(length = 100)
    private String spelling;

    @Column(length = 255)
    private String meaning;

    private Integer level;

    @Column(length = 100)
    private String pronunciation;

    @Column(name = "audio_url", length = 500)
    private String audioUrl;
}