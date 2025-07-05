package com.apartment.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@TableName("room")
@Data

public class Room {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String pic;
    private String hasPeople;
    private Integer apartmentId;
    private BigDecimal price;
    private String password;

    @TableField(exist = false)
    private String apartmentName;

}
