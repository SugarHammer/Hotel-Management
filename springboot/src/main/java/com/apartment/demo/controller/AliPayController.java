package com.apartment.demo.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.apartment.demo.controller.dto.AliPay;
import com.apartment.demo.mapper.OrderMapper;
import com.apartment.demo.service.AliPayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/alipay")
public class AliPayController {

    @Resource
    private AliPayService aliPayService;

    @GetMapping("/pay")
    public String pay(AliPay aliPay) {
        return aliPayService.pay(aliPay);
    }


    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        return aliPayService.payNotify(request);
    }

}
