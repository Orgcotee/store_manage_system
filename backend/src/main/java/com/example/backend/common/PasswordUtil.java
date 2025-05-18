package com.example.backend.common;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtil {
    /**
     生成盐值
     @param strength 强度因子，影响加密的计算成本
     @return 生成的盐值
     */
    public static String generateSalt (int strength) {
        String salt = BCrypt.gensalt (strength);
        return salt.substring(0, 29);
    }
    /**
     根据盐值和明文密码进行加密
     @param rawPassword 明文密码
     @param salt 盐值
     @return 加密后的密码
     */
    public static String encodePassword (String rawPassword, String salt) {
        return BCrypt.hashpw (rawPassword, salt);
    }
    /**
     验证密码是否匹配
     @param rawPassword 明文密码
     @param salt 盐值
     @param encodedPassword 已加密需要比较的密码
     @return 如果匹配返回 true，否则返回 false
     */
    public static boolean matchesPassword (String rawPassword, String salt, String encodedPassword) {
        String newEncodedPassword = BCrypt.hashpw (rawPassword, salt);
        return newEncodedPassword.equals (encodedPassword);
    }
}