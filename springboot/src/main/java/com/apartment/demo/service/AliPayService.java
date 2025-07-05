package com.apartment.demo.service;

import com.apartment.demo.controller.dto.AliPay;

import javax.servlet.http.HttpServletRequest;

public interface AliPayService {

    String pay(AliPay aliPay);

    String payNotify(HttpServletRequest request) throws Exception;
}
