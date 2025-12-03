package com.accuresoftech.abc.services;

import com.accuresoftech.abc.entity.auth.RefreshToken;

public interface RefreshTokenService {

    /** Creates and returns raw refresh token */
    String createRefreshToken(Long userId);

    /** Returns new rotated raw token */
    String rotateToken(String oldRawToken, Long userId);

    boolean validate(String rawToken);

    Long findUserId(String rawToken);

    void revoke(String rawToken);

    void revokeAll(Long userId);
}
