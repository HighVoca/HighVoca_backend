-- initial_words.sql
-- part_of_speech 컬럼 추가 (기존 테이블에 컬럼이 없는 경우)
-- ALTER TABLE word ADD COLUMN part_of_speech VARCHAR(50) AFTER pronunciation;

-- 샘플 INSERT (part_of_speech 포함)
INSERT INTO word (spelling, meaning, level, pronunciation, part_of_speech, audio_url) VALUES
-- Level 1 (Starter)
('apple', '사과', 1, 'ˈæpəl', 'Noun', NULL),
('run', '달리다', 1, 'rʌn', 'Verb', NULL),
('big', '큰', 1, 'bɪɡ', 'Adjective', NULL),
('happy', '행복한', 1, 'ˈhæpi', 'Adjective', NULL),
('book', '책', 1, 'bʊk', 'Noun', NULL),

-- Level 5 (Beginner)
('achieve', '달성하다', 5, 'əˈtʃiːv', 'Verb', NULL),
('environment', '환경', 5, 'ɪnˈvaɪrənmənt', 'Noun', NULL),
('significant', '중요한', 5, 'sɪɡˈnɪfɪkənt', 'Adjective', NULL),
('frequently', '자주', 5, 'ˈfriːkwəntli', 'Adverb', NULL),
('opportunity', '기회', 5, 'ˌɑːpərˈtuːnəti', 'Noun', NULL),

-- Level 10 (Intermediate)
('compromise', '타협하다', 10, 'ˈkɑːmprəmaɪz', 'Verb', NULL),
('inevitable', '불가피한', 10, 'ɪnˈevɪtəbl', 'Adjective', NULL),
('perspective', '관점', 10, 'pərˈspektɪv', 'Noun', NULL),
('thoroughly', '철저히', 10, 'ˈθɜːrəli', 'Adverb', NULL),
('distinguish', '구별하다', 10, 'dɪˈstɪŋɡwɪʃ', 'Verb', NULL),

-- Level 15 (Advanced)
('ambiguous', '모호한', 15, 'æmˈbɪɡjuəs', 'Adjective', NULL),
('deteriorate', '악화되다', 15, 'dɪˈtɪriəreɪt', 'Verb', NULL),
('unprecedented', '전례 없는', 15, 'ʌnˈpresɪdentɪd', 'Adjective', NULL),
('jurisdiction', '관할권', 15, 'ˌdʒʊrɪsˈdɪkʃən', 'Noun', NULL),
('predominantly', '주로', 15, 'prɪˈdɑːmɪnəntli', 'Adverb', NULL),

-- Level 20 (Expert)
('quintessential', '전형적인', 20, 'ˌkwɪntɪˈsenʃəl', 'Adjective', NULL),
('juxtaposition', '병치', 20, 'ˌdʒʌkstəpəˈzɪʃən', 'Noun', NULL),
('exacerbate', '악화시키다', 20, 'ɪɡˈzæsərbeɪt', 'Verb', NULL),
('surreptitiously', '몰래', 20, 'ˌsɜːrəpˈtɪʃəsli', 'Adverb', NULL),
('recalcitrant', '반항적인', 20, 'rɪˈkælsɪtrənt', 'Adjective', NULL);

-- 기존 데이터의 part_of_speech 업데이트 예시
-- UPDATE word SET part_of_speech = 'Noun' WHERE spelling = 'apple';
-- UPDATE word SET part_of_speech = 'Verb' WHERE spelling = 'run';
