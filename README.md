# 📚 HighVoca - Backend

> **하이보카(HighVoca)**: 효율적인 단어 학습과 퀴즈 서비스를 제공하는 단어장 앱 하이보카의 백엔드입니다.

---

## 🛠️ 기술 스택 (Tech Stack)

- **Framework**: Spring Boot 3.5.9
- **Language**: Java 21
- **Build Tool**: Gradle (Groovy)
- **Database**: MySQL, Spring Data JPA
- **Security**: Spring Security, JWT
- **API Docs**: Swagger (SpringDoc)

---

### 🌐 Git-flow 전략 (Git-flow Strategy)

- **`main`**: 최종적으로 서비스가 운영되는 가장 안정적인 버전 브랜치
- **`develop`**: 다음 버전 개발을 위한 중심 브랜치. 기능 개발 완료 후 `feature` 브랜치들이 병합
- **`feature`**: 세부 기능 개발용 브랜치. `develop`에서 분기하여 작업

---

### 📌 브랜치 규칙 및 네이밍 (Branch Rules & Naming)

1. 모든 기능 개발은 **feature** 브랜치에서 시작
2. 작업 시작 전, 항상 최신 `develop` 내용 받아오기 (`git pull origin develop`)
3. 작업 완료 후, `develop`으로 Pull Request(PR) 생성
4. **브랜치 이름 형식:** `feature/이슈번호-기능명`
    - 예시: `feature/1-voca-crud`, `feature/5-login-api`

---

### 🎯 커밋 컨벤션 (Commit Convention)

- **주의 사항:**
    - `type`은 소문자만 사용합니다.
    - `subject`는 현재형 동사를 사용하여 간결하게 작성합니다.

#### 📋 타입 목록

| type | 설명 |
| :--- | :--- |
| `start` | 프로젝트 초기 설정 및 환경 구축 |
| `feat` | 새로운 기능 추가 (API, 비즈니스 로직 등) |
| `fix` | 버그 수정 |
| `refactor` | 기능 변경 없는 코드 리팩토링 |
| `settings` | 설정 파일 변경 (`build.gradle`, `yml` 등) |
| `docs` | 문서 수정 (README, API 명세 등) |
| `test` | 테스트 코드 추가 및 수정 |
| `chore` | 기타 자잘한 작업, 빌드 업무 수정 |
| `db` | 데이터베이스 스키마(ERD) 변경 및 DDL 수정 |
| `rename` | 파일/폴더명 수정 혹은 이동 |
| `remove` | 파일 삭제 |

```bash
#### ✨ 예시
feat: 단어장 조회 API 구현
fix: 회원가입 시 중복 검사 로직 오류 수정
settings: MySQL 커넥션 풀 설정 추가