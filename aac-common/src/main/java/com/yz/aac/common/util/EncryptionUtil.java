package com.yz.aac.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.Entry;

@SuppressWarnings("unused")
@Slf4j
public class EncryptionUtil {

    public static String createToken(Map<String, Object> claims, long ttlMillis, String securityKey) {
        SignatureAlgorithm alg = SignatureAlgorithm.HS256;
//        Key signingKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(encodeBase64(securityKey)), alg.getJcaName());
        Key signingKey = new SecretKeySpec(securityKey.getBytes(), alg.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", alg.getValue())
                .setClaims(claims)
                .signWith(alg, signingKey);
        if (ttlMillis > 0) {
            long nowMillis = System.currentTimeMillis();
            long expMillis = nowMillis + ttlMillis;
            builder
                    .setExpiration(new Date(expMillis));
//                    .setNotBefore(new Date(nowMillis));
        }
        return builder.compact();
    }

    public static Map<String, Object> parseToken(String token, String securityKey) {
        Map<String, Object> result = null;
        try {
            Claims claims = Jwts.parser()
//                    .setSigningKey(DatatypeConverter.parseBase64Binary(encodeBase64(securityKey)))
                    .setSigningKey(securityKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            result = new HashMap<>();
            for (Entry<String, Object> entry : claims.entrySet()) {
                result.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return result;
    }

    private static String encodeBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(UTF_8));
    }

}
