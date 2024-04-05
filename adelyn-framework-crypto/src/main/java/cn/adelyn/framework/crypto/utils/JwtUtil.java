package cn.adelyn.framework.crypto.utils;

import cn.adelyn.framework.core.execption.AdelynException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class JwtUtil {
    public static String generateToken(String subject, Date signedAt, long validTime, PrivateKey privateKey){
        Date expiration = new Date(signedAt.getTime() + validTime);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(signedAt)
                .expiration(expiration)
                .signWith(privateKey)
                .compact();
    }

    public static String validateToken(String token, Date issuedAt, PublicKey publicKey) {
        try {
            Claims claims = Jwts.parser()
                    .clock(() -> issuedAt)
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            throw new AdelynException("token is invalid", e);
        }
    }
}