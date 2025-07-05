package com.apartment.demo.mapper;

import com.apartment.demo.controller.dto.ContractCountDto;
import com.apartment.demo.entity.Contract;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContractMapper extends BaseMapper<Contract> {

    Contract findContractByUserId(@Param("userId")Integer userId);

    Page<Contract> findAllContract(Page page,@Param("userName") String search,@Param("userId") Integer userId);

    @Select("select count(a.id) count, a.`name` from apartment a,contract c where c.apartment_id = a.id GROUP BY a.id")
    List<ContractCountDto> countContract();
}
