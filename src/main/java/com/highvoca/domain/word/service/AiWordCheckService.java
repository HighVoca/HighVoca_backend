package com.highvoca.domain.word.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.highvoca.domain.word.dto.WordStudyDto.CheckAnswerRequest;
import com.highvoca.domain.word.dto.WordStudyDto.CheckAnswerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiWordCheckService {

    private final ChatClient.Builder chatClientBuilder;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT =
            "You are an English vocabulary grading assistant. " +
            "The student is given an English word with its correct Korean meaning and submits their own Korean answer. " +
            "Determine if the student's answer is correct.\n\n" +
            "IMPORTANT GRADING RULES:\n" +
            "- Accept synonyms or very close meanings as correct.\n" +
            "- Be lenient with typos: even if the user's input has minor typos or slight spelling mistakes " +
            "(e.g., '사과' vs '사과ㅏ', '바나나' vs '바나ㅏ나'), if the core meaning clearly matches the intended answer, " +
            "you MUST consider it correct (isCorrect: true).\n" +
            "- Only mark as incorrect when the answer is a completely different word or meaning.\n\n" +
            "Respond ONLY with a JSON object in this exact format (no markdown, no explanation):\n" +
            "{\"isCorrect\": true} if correct,\n" +
            "{\"isCorrect\": false, \"feedback\": \"<short Korean feedback within 50 chars>\"} if incorrect.";

    public CheckAnswerResponse check(CheckAnswerRequest request) {
        String userPrompt = String.format(
                "Word: %s\nCorrect meaning: %s\nStudent answer: %s",
                request.getSpelling(), request.getMeaning(), request.getUserInput()
        );

        try {
            ChatClient chatClient = chatClientBuilder.build();

            String raw = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(userPrompt)
                    .call()
                    .content();

            return parseResponse(request.getWordId(), raw);
        } catch (Exception e) {
            log.error("AI 단어 채점 실패: {}", e.getMessage());
            return fallbackCheck(request);
        }
    }

    private CheckAnswerResponse parseResponse(Long wordId, String raw) {
        try {
            JsonNode node = objectMapper.readTree(raw);
            boolean isCorrect = node.get("isCorrect").asBoolean();
            String feedback = isCorrect ? null : node.path("feedback").asText(null);

            return CheckAnswerResponse.builder()
                    .wordId(wordId)
                    .isCorrect(isCorrect)
                    .feedback(feedback)
                    .build();
        } catch (Exception e) {
            log.warn("AI 응답 파싱 실패, raw={}", raw, e);
            throw new RuntimeException("AI 응답 파싱 실패");
        }
    }

    private CheckAnswerResponse fallbackCheck(CheckAnswerRequest request) {
        boolean isCorrect = request.getMeaning().trim().equalsIgnoreCase(request.getUserInput().trim());
        return CheckAnswerResponse.builder()
                .wordId(request.getWordId())
                .isCorrect(isCorrect)
                .feedback(isCorrect ? null : "정확한 뜻은 '" + request.getMeaning() + "'입니다.")
                .build();
    }
}
