package com.highvoca.domain.auth.service;

import com.highvoca.domain.auth.dto.AuthDto;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import com.highvoca.global.jwt.JwtTokenProvider;
import com.highvoca.global.oauth.code.AuthCodeStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final AuthCodeStore authCodeStore;

    @Transactional
    public AuthDto.TokenResponse exchangeCode(AuthDto.CodeExchangeRequest request) {
        // 1. 코드 소비 (일회용 - 꺼내면서 즉시 삭제)
        String email = authCodeStore.consumeCode(request.getCode());
        if (email == null) {
            throw new RuntimeException("유효하지 않거나 만료된 인가 코드입니다.");
        }

        // 2. 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        // 3. 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(email, user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        // 4. 리프레시 토큰 DB 저장
        user.updateRefreshToken(refreshToken);

        log.info("🔐 코드 교환 완료, 토큰 발급: {}", email);

        return new AuthDto.TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthDto.TokenResponse reissue(AuthDto.ReissueRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않거나 만료된 Refresh Token입니다.");
        }

        String email = jwtTokenProvider.getEmail(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new RuntimeException("토큰 정보가 일치하지 않습니다.");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(email, user.getRole().name());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        user.updateRefreshToken(newRefreshToken);

        log.info("🔄 토큰 재발급 완료: {}", email);

        return new AuthDto.TokenResponse(newAccessToken, newRefreshToken);
    }
}