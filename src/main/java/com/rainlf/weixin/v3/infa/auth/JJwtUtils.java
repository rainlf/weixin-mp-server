package com.rainlf.weixin.v3.infa.auth;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author rain
 * @date 7/19/2024 5:39 PM
 */
public class JJwtUtils {
    private static final String SIGNER = "rain";
    private static final String SECRET = "rain-012345679";
    private static final long defaultExpire = 1000 * 60 * 60 * 24 * 180L; //默认过期时间180天

    //创建一个jwt密钥 加密和解密都需要用这个玩意
    private static final SecretKey key = Jwts.SIG.HS256.key()
            .random(new SecureRandom(SECRET.getBytes(StandardCharsets.UTF_8)))
            .build();

    /**
     * 使用默认过期时间（7天），生成一个JWT
     */
    public static String createToken(String subject) {
        return createToken(subject, null, defaultExpire);
    }

    /**
     * 生成token
     */
    public static String createToken(String subject, Map<String, Object> claims, Long expire) {
        JwtBuilder builder = Jwts.builder();
        Date now = new Date();
        // 生成token
        builder.id(UUID.randomUUID().toString()) //id 这个可以不填，但是建议填
                .issuer(SIGNER) //签发者
                .claims(claims) //数据
                .subject(subject) //主题
                .issuedAt(now) //签发时间
                .expiration(new Date(now.getTime() + expire)) //过期时间
                .signWith(key); //签名方式
//        builder.header().add("JWT", "JSpWdhuPGblNZApVclmX");
        return builder.compact();
    }

    /**
     * 解析token
     */
    public static Claims claims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("invalid jwt token, token expired");
        } catch (JwtException e) {
            throw new RuntimeException("invalid jwt token, token invalid");
        } catch (Exception e) {
            throw new RuntimeException("invalid jwt token, parse token error");
        }
    }
}
