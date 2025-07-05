package com.apartment.demo.service.Impl;

import com.apartment.demo.entity.User;
import com.apartment.demo.mapper.UserMapper;
import com.apartment.demo.service.BaseService;
import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class BaseServiceImpl implements BaseService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private HttpServletRequest request;

    @Override
    public User getUser() {
        String token = request.getHeader("token");
        String aud = JWT.decode(token).getAudience().get(0);
        Integer userId = Integer.valueOf(aud);
        return userMapper.selectById(userId);
    }
}
