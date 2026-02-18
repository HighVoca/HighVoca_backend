package com.highvoca.domain.study.service;

import com.highvoca.domain.study.dto.SessionDto.FinishResponse;
import com.highvoca.domain.study.dto.SessionDto.SessionRequest;
import com.highvoca.domain.study.dto.SessionDto.SessionWordResult;
import com.highvoca.domain.study.entity.StudySession;
import com.highvoca.domain.study.entity.UserWordProgress;
import com.highvoca.domain.study.entity.WrongWordHistory;
import com.highvoca.domain.study.repository.StudySessionRepository;
import com.highvoca.domain.study.repository.UserWordProgressRepository;
import com.highvoca.domain.study.repository.WrongWordHistoryRepository;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import com.highvoca.domain.word.entity.Word;
import com.highvoca.domain.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserRepository userRepository;
    private final WordRepository wordRepository;
    private final StudySessionRepository studySessionRepository;
    private final UserWordProgressRepository userWordProgressRepository;
    private final WrongWordHistoryRepository wrongWordHistoryRepository;

    @Transactional
    public FinishResponse finishSession(Long userId, SessionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        int totalCount = request.getResults().size();
        int correctCount = (int) request.getResults().stream()
                .filter(SessionWordResult::getIsCorrect)
                .count();
        int score = (int) ((double) correctCount / totalCount * 100);
        double accuracy = (double) correctCount / totalCount * 100;

        // Update UserWordProgress and WrongWordHistory per word
        for (SessionWordResult result : request.getResults()) {
            Word word = wordRepository.findById(result.getWordId())
                    .orElseThrow(() -> new IllegalArgumentException("단어를 찾을 수 없습니다. id=" + result.getWordId()));

            UserWordProgress progress = userWordProgressRepository
                    .findByUserIdAndWordId(userId, word.getId())
                    .orElse(null);

            if (result.getIsCorrect()) {
                if (progress == null) {
                    progress = UserWordProgress.builder()
                            .user(user)
                            .word(word)
                            .currentStage(1)
                            .lastReviewedAt(java.time.LocalDateTime.now())
                            .nextReviewAt(java.time.LocalDateTime.now().plusHours(1))
                            .build();
                } else {
                    progress.incrementStage();
                }
            } else {
                if (progress == null) {
                    progress = UserWordProgress.builder()
                            .user(user)
                            .word(word)
                            .currentStage(0)
                            .lastReviewedAt(java.time.LocalDateTime.now())
                            .nextReviewAt(java.time.LocalDateTime.now())
                            .build();
                } else {
                    progress.resetStage();
                }

                WrongWordHistory wrongHistory = WrongWordHistory.builder()
                        .user(user)
                        .word(word)
                        .wrongDate(LocalDate.now())
                        .build();
                wrongWordHistoryRepository.save(wrongHistory);
            }
            userWordProgressRepository.save(progress);
        }

        // Update streak
        user.updateStreak();

        // Save session
        StudySession session = StudySession.builder()
                .user(user)
                .totalCount(totalCount)
                .score(score)
                .accuracy(accuracy)
                .build();
        studySessionRepository.save(session);

        return FinishResponse.builder()
                .sessionId(session.getId())
                .streakCnt(user.getStreakCnt())
                .score(score)
                .accuracy(accuracy)
                .build();
    }
}
