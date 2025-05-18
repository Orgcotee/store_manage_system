package com.example.backend.anno;

import java.lang.annotation.*;

/**
 *
 * 鉴权功能实现
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {
    /**
     * 权限标识符
     */
    public String value() default "";
}
