package com.apartment.demo.mapper;

import com.apartment.demo.controller.dto.EngineerCountDto;
import com.apartment.demo.entity.Engineer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EngineerMapper extends BaseMapper<Engineer>{

    Page<Engineer> findAllEngineerList(Page page,
                                       @Param("userId") Integer userId,@Param("adminId") Integer adminId,
                                       @Param("engineerId") Integer engineerId,@Param("state") String state,
                                       @Param("search") String search);

    Engineer findself(@Param("userId")Integer userId);


    @Select("select count(id) count, state from engineer GROUP BY state")
    List<EngineerCountDto> countState();

}
