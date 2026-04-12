package com.highvoca.domain.study.service;

import com.highvoca.domain.study.dto.VocabularyDto;
import com.highvoca.domain.study.entity.UserWordProgress;
import com.highvoca.domain.study.entity.WrongWordHistory;
import com.highvoca.domain.study.repository.UserWordProgressRepository;
import com.highvoca.domain.study.repository.WrongWordHistoryRepository;
import com.highvoca.domain.word.entity.Word;
import com.highvoca.domain.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VocabularyService {

    private final UserWordProgressRepository userWordProgressRepository;
    private final WrongWordHistoryRepository wrongWordHistoryRepository;
    private final WordRepository wordRepository;

    // 1. 날짜별 단어장 조회 (기존 기능)
    public List<VocabularyDto.VocabularyResponse> getVocabularies(Long userId, LocalDate date, String filter) {
        if ("WRONG".equalsIgnoreCase(filter)) {
            List<WrongWordHistory> wrongs = wrongWordHistoryRepository.findByUserIdAndWrongDate(userId, date);
            return wrongs.stream().map(w -> VocabularyDto.VocabularyResponse.from(w.getWord(), true)).collect(Collectors.toList());
        } else if ("LEARNED".equalsIgnoreCase(filter)) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            List<UserWordProgress> progresses = userWordProgressRepository.findByUserIdAndLastReviewedAtBetween(userId, startOfDay, endOfDay);
            List<WrongWordHistory> wrongs = wrongWordHistoryRepository.findByUserIdAndWrongDate(userId, date);

            Set<Long> wrongWordIds = wrongs.stream().map(w -> w.getWord().getId()).collect(Collectors.toSet());
            Map<Long, Word> studiedWordsMap = new HashMap<>();
            progresses.forEach(p -> studiedWordsMap.put(p.getWord().getId(), p.getWord()));
            wrongs.forEach(w -> studiedWordsMap.put(w.getWord().getId(), w.getWord()));

            return studiedWordsMap.values().stream()
                    .map(word -> VocabularyDto.VocabularyResponse.from(word, wrongWordIds.contains(word.getId())))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public VocabularyDto.VocabularyPageResponse getAllVocabularies(Long userId, Integer level, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Word> wordPage = wordRepository.findByLevel(level, pageable);

        List<Long> wordIds = wordPage.getContent().stream().map(Word::getId).collect(Collectors.toList());
        Set<Long> wrongWordIds = wordIds.isEmpty() ? new HashSet<>() : wrongWordHistoryRepository.findWrongWordIdsByUserIdAndWordIdIn(userId, wordIds);

        final Set<Long> finalWrongWordIds = wrongWordIds;

        List<VocabularyDto.VocabularyResponse> content = wordPage.getContent().stream()
                .map(word -> VocabularyDto.VocabularyResponse.from(word, finalWrongWordIds.contains(word.getId())))
                .collect(Collectors.toList());

        VocabularyDto.PageInfo pageInfo = VocabularyDto.PageInfo.builder()
                .currentPage(wordPage.getNumber())
                .totalPages(wordPage.getTotalPages())
                .totalElements(wordPage.getTotalElements())
                .hasNext(wordPage.hasNext()).build();

        return VocabularyDto.VocabularyPageResponse.builder().content(content).pageInfo(pageInfo).build();
    }
}