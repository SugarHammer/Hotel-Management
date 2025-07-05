package com.apartment.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author
 * @version 1.0
 * @Description 默认密码枚举
 * @Date 2025/12/3 22:25
 */
@Getter
@AllArgsConstructor
public enum PwdEnum {

    /**
     * 默认密码
     */
    PASSWORD("123"),

    /**
     * 默认门锁密码
     */
    ROOMPASSWORD("123123");

    private final String password;
}
