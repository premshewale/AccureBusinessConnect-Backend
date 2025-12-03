package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.entity.auth.RefreshToken;
import com.accuresoftech.abc.repository.RefreshTokenRepository;
import com.accuresoftech.abc.utils.HmacUtil;
import com.accuresoftech.abc.services.RefreshTokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repo;

    @Value("${jwt.refresh-secret:${jwt.secret}}")
    private String refreshSecret;

    @Value("${jwt.refresh-ttl-days:30}")
    private int ttlDays;

    private String hash(String raw) {
        return HmacUtil.hmacSha256(refreshSecret, raw);
    }

    private RefreshToken saveToken(Long userId, String raw) {
        String hashed = hash(raw);

        RefreshToken token = RefreshToken.builder()
                .tokenHash(hashed)
                .userId(userId)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plus(ttlDays, ChronoUnit.DAYS))
                .revoked(false)
                .build();

        return repo.save(token);
    }

    @Override
    public String createRefreshToken(Long userId) {
        String raw = UUID.randomUUID().toString();
        saveToken(userId, raw);
        return raw;
    }

    @Override
    public boolean validate(String rawToken) {
        String hashed = hash(rawToken);
        Optional<RefreshToken> opt = repo.findByTokenHash(hashed);

        if (opt.isEmpty()) return false;
        RefreshToken t = opt.get();

        return !t.isRevoked() && t.getExpiresAt().isAfter(Instant.now());
    }

    @Override
    public Long findUserId(String rawToken) {
        String hashed = hash(rawToken);
        return repo.findByTokenHash(hashed)
                .filter(t -> !t.isRevoked())
                .map(RefreshToken::getUserId)
                .orElse(null);
    }

    @Override
    public void revoke(String rawToken) {
        String hashed = hash(rawToken);
        repo.findByTokenHash(hashed).ifPresent(t -> {
            t.setRevoked(true);
            repo.save(t);
        });
    }

    @Override
    public void revokeAll(Long userId) {
        repo.findByUserIdAndRevokedFalse(userId)
                .forEach(t -> {
                    t.setRevoked(true);
                    repo.save(t);
                });
    }

    @Override
    public String rotateToken(String oldRawToken, Long userId) {
        revoke(oldRawToken);
        return createRefreshToken(userId);
    }
}
