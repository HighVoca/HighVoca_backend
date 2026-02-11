# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

HighVoca is a vocabulary learning and quiz service backend built with **Spring Boot 3.5.9** and **Java 21**. It provides OAuth2 social login (Kakao), adaptive level testing with AI feedback (OpenAI), and word study tracking.

## Build & Run Commands

```bash
# Build
./gradlew clean build

# Build without tests
./gradlew clean build -x test

# Run
./gradlew bootRun

# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests "com.highvoca.SomeTest"
```

## Required Environment Variables

`DB_PASSWORD`, `JWT_SECRET`, `OPENAI_API_KEY`, `KAKAO_CLIENT_ID`, `KAKAO_CLIENT_SECRET`

Database: MySQL on `localhost:3307`, schema `highvoca`.

## Architecture

Domain-driven layered architecture under `com.highvoca`:

- **`domain/{module}/`** — Each feature module contains its own `controller/`, `service/`, `dto/`, `entity/`, `repository/` subpackages
  - `auth` — JWT token reissue (`/api/v1/auth/**`)
  - `user` — User entity, enums (Role, SocialProvider), repository
  - `word` — Word entity with level (1-20), repository with native random queries
  - `study` — StudySession, UserWordProgress, WrongWordHistory entities
  - `leveltest` — 4-step adaptive level test with AI feedback (`/api/v1/level-test/**`)
- **`global/`** — Cross-cutting infrastructure
  - `config/` — SecurityConfig (OAuth2 + JWT), SwaggerConfig, WebMvcConfig (CORS)
  - `jwt/` — JwtTokenProvider (access: 30min, refresh: 7days with rotation)
  - `oauth/` — Kakao OAuth2 flow (CustomOAuth2UserService, OAuth2SuccessHandler)
- **`common/`** — BaseTimeEntity (JPA auditing for createdAt/updatedAt)

## Key Patterns & Conventions

- **Entities**: Use Lombok (`@Getter`, `@Builder`, `@NoArgsConstructor(access = PROTECTED)`, `@DynamicInsert`), extend `BaseTimeEntity` for timestamps
- **DTOs**: Inner static classes within a module DTO class (e.g., `LevelTestDto.Step1Request`, `AuthDto.ReissueRequest`)
- **Services**: `@RequiredArgsConstructor` constructor injection, `@Transactional` for writes, `@Slf4j` logging
- **Repositories**: `JpaRepository` with `@Query(nativeQuery = true)` for MySQL-specific queries (e.g., `ORDER BY RAND()`)
- **API responses**: Standardized wrapper with `isSuccess`, `code`, `message`, `result` fields

## Git Conventions

- **Branching**: Git-flow — `main` (production), `develop` (integration), `feature/<issue-number>-<name>`
- **Commits**: `<type>: <subject>` in Korean. Types: `feat`, `fix`, `refactor`, `settings`, `docs`, `deploy`, `test`, `chore`, `db`, `rename`, `remove`
- **PRs**: Feature branches merge into `develop`

## Deployment

GitHub Actions (`.github/workflows/deploy.yml`) triggers on push to main: Gradle build → Docker image → push to Docker Hub → SSH deploy to EC2. Dockerfile uses `eclipse-temurin:21-jdk`.
