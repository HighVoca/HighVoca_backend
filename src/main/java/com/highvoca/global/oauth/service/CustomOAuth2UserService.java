package com.highvoca.global.oauth.service;

import com.highvoca.domain.user.entity.User;
import com.highvoca.domain.user.enums.Role;
import com.highvoca.domain.user.enums.SocialProvider;
import com.highvoca.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        if (email == null || email.isEmpty()) {
            Long id = (Long) attributes.get("id");
            email = "kakao_" + id + "@highvoca.com";
        }

        String nickname = (String) profile.get("nickname");

        log.info("📌 카카오 로그인 로직 실행: email={}", email);

        saveOrUpdate(email, nickname);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_USER.name())),
                attributes,
                "id"
        );
    }

    private User saveOrUpdate(String email, String nickname) {
        User user = userRepository.findByEmail(email)
                .orElse(User.builder()
                        .email(email)
                        .username(nickname)
                        .role(Role.ROLE_USER)
                        .provider(SocialProvider.KAKAO)
                        .build());

        return userRepository.save(user);
    }
}