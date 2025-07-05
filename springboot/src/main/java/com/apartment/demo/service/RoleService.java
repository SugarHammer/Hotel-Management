package com.apartment.demo.service;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Role;

public interface RoleService {

    Result<?> SaveRole(Role role);

    Result<?> UpdateRole(Role role);

    Result<?> ChangeRolePermission(Role role);

    Result<?> DeleteRoleById(Long id);

    Result<?> FindRoleById(Long id);

    Result<?> FindAllRole();

    Result<?> FindAllRoleByPage(Integer pageNum, Integer pageSize, String search);
}
