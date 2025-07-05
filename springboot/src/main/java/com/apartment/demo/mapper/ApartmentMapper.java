package com.apartment.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.apartment.demo.entity.Apartment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ApartmentMapper extends BaseMapper<Apartment> {

    Page<Apartment> findAllApartment(Page page, @Param("name") String name, @Param("adminId")Integer adminId);

    List<Apartment> findAllApartmentList();

    Apartment findApartmentByRoomId(@Param("roomId") Integer roomId);

    @Select("select * from apartment where admin_id = #{adminId}")
    Apartment findApartmentByAdminId(@Param("adminId") Integer adminId);

    List<Apartment> findApartmentByRandom();
}
