package com.highvoca.domain.word.service;

import com.highvoca.domain.study.entity.UserWordProgress;
import com.highvoca.domain.study.repository.UserWordProgressRepository;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import com.highvoca.domain.word.dto.WordStudyDto.WordStudyResponse;
import com.highvoca.domain.word.entity.Word;
import com.highvoca.domain.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordStudyService {

    private final UserRepository userRepository;
    private final WordRepository wordRepository;
    private final UserWordProgressRepository userWordProgressRepository;

    @Transactional(readOnly = true)
    public List<WordStudyResponse> getTodayWords(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        int dailyGoal = user.getDailyGoal();
        List<WordStudyResponse> result = new ArrayList<>();

        // Priority 1: Review words (next_review_at <= now)
        List<UserWordProgress> reviewWords = userWordProgressRepository.findReviewableWords(userId, LocalDateTime.now());
        int reviewCount = Math.min(reviewWords.size(), dailyGoal);
        for (int i = 0; i < reviewCount; i++) {
            result.add(WordStudyResponse.from(reviewWords.get(i).getWord(), true));
        }

        // Priority 2: New words (fill remaining slots)
        int remaining = dailyGoal - result.size();
        if (remaining > 0) {
            int userLevel = (int) Math.round(user.getLevel());
            int minLevel = Math.max(1, userLevel - 2);
            int maxLevel = Math.min(20, userLevel + 2);

            List<Word> newWords = wordRepository.findNewWordsForUser(userId, minLevel, maxLevel, remaining);
            for (Word word : newWords) {
                result.add(WordStudyResponse.from(word, false));
            }
        }

        return result;
    }

    @Transactional(readOnly = true)
    public List<WordStudyResponse> getExtraWords(Long userId, int count) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        int userLevel = (int) Math.round(user.getLevel());
        int minLevel = Math.max(1, userLevel - 2);
        int maxLevel = Math.min(20, userLevel + 2);

        // Priority: unstudied words within level range
        List<Word> words = new ArrayList<>(wordRepository.findNewWordsForUser(userId, minLevel, maxLevel, count));

        // Fallback: fill remaining with any random words
        if (words.size() < count) {
            int remaining = count - words.size();
            List<Long> excludeIds = words.stream().map(Word::getId).toList();
            if (excludeIds.isEmpty()) {
                excludeIds = List.of(0L);
            }
            List<Word> fallbackWords = wordRepository.findRandomWordsExcluding(excludeIds, remaining);
            words.addAll(fallbackWords);
        }

        return words.stream()
                .map(word -> WordStudyResponse.from(word, false))
                .toList();
    }
}
