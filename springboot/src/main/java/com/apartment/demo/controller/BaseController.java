package com.apartment.demo.controller;

import com.apartment.demo.service.BaseService;
import com.apartment.demo.entity.User;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class BaseController {

    @Resource
    BaseService baseService;

    /**
     * 根据token获取用户信息
     * @return user
     */
    public User getUser() {
        return baseService.getUser();
    }
}
