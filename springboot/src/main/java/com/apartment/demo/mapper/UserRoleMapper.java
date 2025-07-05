package com.apartment.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.apartment.demo.entity.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Delete("delete from user_role where user_id=#{userId}")
    void deleteByUserRoleId(@Param("userId") Long userId);

    @Update("update user_role set role_id = #{userRole.roleId} where user_id=#{userRole.userId}")
    void updateByUserId(@Param("userRole") UserRole userRole);

    @Select("select * from user_role where user_id=#{userId}")
    UserRole selectByUserId(@Param("userId") Integer userId);

}
