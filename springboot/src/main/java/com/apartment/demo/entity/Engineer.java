package com.apartment.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("engineer")
public class Engineer {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String text;
    private Integer userId;
    private Integer adminId;
    private Integer engineerId;
    private Integer apartmentId;
    private Integer roomId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;
    private Integer state;

    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String adminName;
    @TableField(exist = false)
    private String apartmentName;
    @TableField(exist = false)
    private String roomName;
    @TableField(exist = false)
    private String engineerName;
}
