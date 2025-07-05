package com.apartment.demo.mapper;

import com.apartment.demo.controller.dto.UserAddressDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.apartment.demo.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {

    Page<User> findAllUser(Page page,@Param("nickName") String nickName);

    Page<User> findAllConcractUser(Page page,@Param("nickName") String nickName,@Param("adminId") Integer adminId);

    List<User> findAllAdmin();

    List<User> findAllEngineer();

    List<User> findAllUserNoContract();

    @Select("select count(id) count, address from user GROUP BY address")
    List<UserAddressDto> countAddress();

    // 查询用户名
    @Select("select password from user where username=#{username}")
    User selectByName(@Param("username") String username);

    @Update("update user set password = #{newPass} where id = #{userId}")
    int updatePass(Map<String, Object> map);
}
