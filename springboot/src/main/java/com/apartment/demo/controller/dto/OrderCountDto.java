package com.apartment.demo.controller.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCountDto {
    private String name;
    private BigDecimal count;
}
