package com.highvoca.domain.auth.service;

import com.highvoca.domain.auth.dto.AuthDto;
import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.repository.UserRepository;
import com.highvoca.global.jwt.JwtTokenProvider;
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

    @Transactional
    public AuthDto.TokenResponse reissue(AuthDto.ReissueRequest request) {
        String refreshToken = request.getRefreshToken();

        // 1. 리프레시 토큰 유효성 검사 (만료 여부 확인)
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않거나 만료된 Refresh Token입니다.");
        }

        // 2. 토큰에서 사용자 이메일 추출
        String email = jwtTokenProvider.getEmail(refreshToken);

        // 3. DB에서 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        // 4. DB에 저장된 리프레시 토큰과 일치하는지 확인 (탈취 방지)
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new RuntimeException("토큰 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 쌍 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(email, user.getRole().name());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        // 6. DB에 새 리프레시 토큰 업데이트 (Rotation)
        user.updateRefreshToken(newRefreshToken);

        log.info("🔄 토큰 재발급 완료: {}", email);

        return new AuthDto.TokenResponse(newAccessToken, newRefreshToken);
    }
}