package com.apartment.demo.service;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Engineer;

public interface EngineerService {

    Result<?> allEngineer();

    Result<?> allEngineerRoom(Integer adminId);

    Result<?> addEngineer(Engineer engineer);

    Result<?> updateEngineer(Engineer engineer);

    Result<?> findAllEngineerList(Integer pageNum, Integer pageSize, String state, Integer userId, String search);

    Result<?> findself(Integer userId);

    Result<?> CountState();
}
