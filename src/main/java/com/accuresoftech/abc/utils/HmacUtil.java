package com.accuresoftech.abc.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import org.springframework.util.StringUtils;

public class HmacUtil {

    private static final String HMAC_ALGO = "HmacSHA256";

    public static String hmacSha256(String secret, String message) {
        if (!StringUtils.hasText(secret) || !StringUtils.hasText(message)) {
            throw new IllegalArgumentException("Secret and message must be provided");
        }
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            mac.init(new SecretKeySpec(secret.getBytes(), HMAC_ALGO));
            byte[] raw = mac.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(raw);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to compute HMAC", ex);
        }
    }
}
