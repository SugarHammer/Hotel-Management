package com.apartment.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author
 * @version 1.0
 * @Description 角色枚举
 * @Date 2025/12/3 21:40
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

    /**
     * 系统管理员
     */
    ADMIN(1, "管理员", "admin"),

    /**
     * 普通用户
     */
    USER(2, "普通用户", "user"),
    /**
     * 维修员
     */
    ENGINEER(3, "维修员", "engineer"),
    /**
     * 酒店管理员
     */
    APMANGER(4, "酒店管理员", "apManager"),
    /**
     * 已签约用户
     */
    CONTRACTUSER(5, "已签约用户", "contractUser"),

    /**
     * 未实名游客
     */
    PASSAGE(6, "未实名游客", "passage");



    /**
     * 角色id
     */
    private final Integer roleId;

    /**
     * 描述
     */
    private final String name;

    /**
     * 权限标签
     */
    private final String label;
}
