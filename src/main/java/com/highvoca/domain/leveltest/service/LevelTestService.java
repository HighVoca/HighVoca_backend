package com.highvoca.domain.leveltest.service;

import com.highvoca.domain.leveltest.dto.LevelTestDto;
import com.highvoca.domain.leveltest.dto.LevelTestDto.*;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import com.highvoca.domain.word.entity.Word;
import com.highvoca.domain.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LevelTestService {

    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final ChatClient.Builder chatClientBuilder;

    private static final int QUESTIONS_PER_STEP = 5;
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 20;

    /**
     * Step 1: 초기 난이도 기반 단어 조회
     */
    public StepResponse getStep1Words(Step1Request request) {
        int level = request.getDifficulty().getStartLevel();
        List<Word> words = wordRepository.findRandomWordsByLevel(level, QUESTIONS_PER_STEP);

        return StepResponse.builder()
                .words(words.stream().map(WordDto::from).toList())
                .build();
    }

    /**
     * Step 2~4: 이전 결과 기반 적응형 단어 조회
     */
    public StepResponse getNextStepWords(StepRequest request) {
        List<QuestionResult> results = request.getResults();

        // wordId로 원래 level 조회
        List<Long> wordIds = results.stream()
                .map(QuestionResult::getWordId)
                .toList();

        List<Word> previousWords = wordRepository.findAllById(wordIds);
        if (previousWords.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 단어 ID가 포함되어 있습니다.");
        }

        // 이전 단계 단어들의 평균 레벨 계산
        int currentLevel = (int) Math.round(
                previousWords.stream()
                        .mapToInt(Word::getLevel)
                        .average()
                        .orElse(1.0)
        );

        // 정답 수 계산
        long correctCount = results.stream()
                .filter(QuestionResult::getIsCorrect)
                .count();

        // 다음 레벨 결정
        int nextLevel = calculateNextLevel(currentLevel, correctCount);

        List<Word> words = wordRepository.findRandomWordsByLevel(nextLevel, QUESTIONS_PER_STEP);

        return StepResponse.builder()
                .words(words.stream().map(WordDto::from).toList())
                .build();
    }

    /**
     * 최종 제출: 점수 계산 + 사용자 레벨 업데이트 + AI 피드백
     */
    @Transactional
    public SubmitResponse submitAndAnalyze(Long userId, SubmitRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<QuestionResult> totalResult = request.getTotalResult();

        // 단어 정보 조회
        List<Long> wordIds = totalResult.stream()
                .map(QuestionResult::getWordId)
                .toList();

        Map<Long, Word> wordMap = wordRepository.findAllById(wordIds).stream()
                .collect(Collectors.toMap(Word::getId, w -> w));

        // 가중 점수 계산 (높은 레벨 단어 정답에 더 높은 가중치)
        double weightedScore = 0;
        double maxPossibleScore = 0;

        for (QuestionResult result : totalResult) {
            Word word = wordMap.get(result.getWordId());
            if (word == null) continue;

            double weight = word.getLevel();
            maxPossibleScore += weight;

            if (result.getIsCorrect()) {
                weightedScore += weight;
            }
        }

        double weightedAccuracy = maxPossibleScore > 0 ? weightedScore / maxPossibleScore : 0;

        // finalLevel 계산 (1~20)
        double finalLevel = Math.round(weightedAccuracy * MAX_LEVEL * 10.0) / 10.0;
        finalLevel = Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, finalLevel));

        // percentile 계산
        String percentile = calculatePercentile(finalLevel);

        // levelName 결정
        String levelName = determineLevelName(finalLevel);

        // 사용자 레벨 업데이트
        user.updateLevel(finalLevel);

        // AI 피드백 생성
        String aiAnalysis = generateAiFeedback(totalResult, wordMap);

        return SubmitResponse.builder()
                .finalLevel(finalLevel)
                .percentile(percentile)
                .levelName(levelName)
                .aiAnalysis(aiAnalysis)
                .build();
    }

    // ===== Private Methods =====

    private int calculateNextLevel(int currentLevel, long correctCount) {
        int nextLevel;

        if (correctCount >= 4) {
            nextLevel = currentLevel + 2;
        } else if (correctCount >= 2) {
            nextLevel = currentLevel;
        } else {
            nextLevel = currentLevel - 2;
        }

        return Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, nextLevel));
    }

    private String calculatePercentile(double finalLevel) {
        if (finalLevel >= 18) return "Top 1%";
        if (finalLevel >= 16) return "Top 5%";
        if (finalLevel >= 14) return "Top 10%";
        if (finalLevel >= 12) return "Top 20%";
        if (finalLevel >= 10) return "Top 30%";
        if (finalLevel >= 8) return "Top 50%";
        if (finalLevel >= 5) return "Top 70%";
        return "Top 90%";
    }

    private String determineLevelName(double finalLevel) {
        if (finalLevel >= 17) return "Expert";
        if (finalLevel >= 13) return "Advanced";
        if (finalLevel >= 8) return "Intermediate";
        if (finalLevel >= 4) return "Beginner";
        return "Starter";
    }

    private String generateAiFeedback(List<QuestionResult> totalResult, Map<Long, Word> wordMap) {
        // 레벨별 정답/오답 요약 생성
        StringBuilder summary = new StringBuilder();
        summary.append("학생의 레벨 테스트 결과입니다:\n");

        Map<Integer, List<QuestionResult>> byLevel = totalResult.stream()
                .filter(r -> wordMap.containsKey(r.getWordId()))
                .collect(Collectors.groupingBy(r -> wordMap.get(r.getWordId()).getLevel()));

        for (Map.Entry<Integer, List<QuestionResult>> entry : byLevel.entrySet()) {
            int level = entry.getKey();
            List<QuestionResult> results = entry.getValue();
            long correct = results.stream().filter(QuestionResult::getIsCorrect).count();
            summary.append(String.format("- Level %d: %d/%d 정답\n", level, correct, results.size()));
        }

        long totalCorrect = totalResult.stream().filter(QuestionResult::getIsCorrect).count();
        summary.append(String.format("총 점수: %d/%d", totalCorrect, totalResult.size()));

        try {
            ChatClient chatClient = chatClientBuilder.build();

            String response = chatClient.prompt()
                    .system("You are a warm and encouraging English teacher. " +
                            "Analyze the student's test results (weaknesses/strengths) " +
                            "and provide a **50-character Korean message** cheering them up.")
                    .user(summary.toString())
                    .call()
                    .content();

            return response;
        } catch (Exception e) {
            log.error("AI 피드백 생성 실패: {}", e.getMessage());
            return "열심히 학습하면 실력이 금방 늘어요! 화이팅!";
        }
    }
}
