package com.apartment.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@TableName("user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private Integer age;
    private String sex;
    private String address;
    private String avatar;
    private String telephone;
    private String truename;
    private String idcode;

    @TableField(exist = false)
    private List<Integer> roles;

    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private Set<Permission> permissions;

    @TableField(exist = false)
    private Set<Permission> permissionsTree;


}
