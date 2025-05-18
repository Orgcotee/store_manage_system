package com.example.backend.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    // 密钥
    private static final String SECRET_KEY = "warehouseSecretKeyWarehouseSecretKeyWarehouseSecretKey";
    // 过期时间，单位为毫秒
    private static final long EXPIRATION_TIME = 86400000;

    /**
     * 根据 id 和 username 创建 token
     * @param id 用户 ID
     * @param username 用户名
     * @return token 字符串
     */
    public static String generateToken(Integer id, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("id", id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 解析 token，获取用户名
     * @param token token 字符串
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * 解析 token，获取用户 ID
     * @param token token 字符串
     * @return 用户 ID
     */
    public static Integer getIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("id", Integer.class);
    }
}