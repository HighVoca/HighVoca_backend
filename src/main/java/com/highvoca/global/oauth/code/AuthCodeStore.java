package com.highvoca.global.oauth.code;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthCodeStore {

    private final Map<String, CodeEntry> store = new ConcurrentHashMap<>();
    private static final long CODE_TTL_SECONDS = 300;

    public void save(String code, String email) {
        store.put(code, new CodeEntry(email, Instant.now().plusSeconds(CODE_TTL_SECONDS)));
    }

    public String consumeCode(String code) {
        CodeEntry entry = store.remove(code);
        if (entry == null || entry.isExpired()) {
            return null;
        }
        return entry.email();
    }

    @Scheduled(fixedRate = 60_000)
    public void cleanup() {
        store.entrySet().removeIf(e -> e.getValue().isExpired());
    }

    private record CodeEntry(String email, Instant expiresAt) {
        boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}