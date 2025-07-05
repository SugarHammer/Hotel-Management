package com.apartment.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.apartment.demo.entity.Role;
import com.apartment.demo.entity.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {

    @Select("select * from user_role where user_id = #{userId}")
    List<UserRole> getUserRoleByUserId(@Param("userId") Integer userId);


    @Delete("delete from user_role where user_id = #{userId}")
    void deleteRoleByUserId(@Param("userId") Integer userId);

    @Insert("insert into user_role(user_id, role_id) values(#{userId}, #{roleId})")
    void insertUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
}
