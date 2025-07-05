package com.apartment.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@TableName("contract")
@Data
@Getter
@Setter
public class Contract {

    @TableId(type = IdType.AUTO)
    private Integer conId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date terminTime;
    private Integer userId;
    private Integer adminId;
    private Integer apartmentId;
    private Integer roomId;


    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String adminName;
    @TableField(exist = false)
    private String apartmentName;
    @TableField(exist = false)
    private String roomName;
}
