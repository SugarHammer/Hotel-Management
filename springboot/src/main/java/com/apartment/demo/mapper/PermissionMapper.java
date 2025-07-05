package com.apartment.demo.mapper;

import com.apartment.demo.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.apartment.demo.entity.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> getRolePermissionByRoleId(@Param("roleId") Integer roleId);

    List<Permission> getRoleChangePermissionByRoleId(@Param("roleId") Integer roleId);

    List<Permission> getChildrenPermissionyPid(@Param("roleId") Integer roleId,@Param("Pid") Integer Pid);

    @Select("Select * from permission where id = #{id}")
    List<Permission> findById(@Param("id") Long id);

    @Delete("delete from role_permission where role_id = #{roleId}")
    void deletePermissionsByRoleId(@Param("roleId") Integer roleId);

    @Insert("insert into role_permission(role_id, permission_id) values(#{roleId}, #{permissionId})")
    void insertRoleAndPermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);

    @Select("Select * from permission")
    List<Permission> selectAll();

    @Select("Select * from permission")
    Page<Permission> selectAllPage(@Param("page") Page page);

}
