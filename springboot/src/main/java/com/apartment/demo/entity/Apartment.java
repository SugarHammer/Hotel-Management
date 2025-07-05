package com.apartment.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@TableName("apartment")
@Data
@Getter
@Setter
public class Apartment {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String address;
    private String pic;
    private Integer adminId;
    private String coordinates;
    @TableField(exist = false)
    private String telephone;

    @TableField(exist = false)
    private String adminName;
}
