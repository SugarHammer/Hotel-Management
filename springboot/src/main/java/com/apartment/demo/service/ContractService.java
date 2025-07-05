package com.apartment.demo.service;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Contract;

import java.util.Map;

public interface ContractService {

    Result<?> alluser();

    Result<?> allroom(Integer adminId);

    Result<?> addContract(Contract contract);

    Result<?> findAllContract(Integer pageNum, Integer pageSize, String search,Integer userId);

    Result<?> findself(Integer userId);

    Result<?> leaveApartment(Integer conId);

    Result<?> findselfHasPic(Integer id);

    Map<String, Object> download(Long id);

    Result<?> CountContract();
}
