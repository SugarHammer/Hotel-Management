package com.apartment.demo.service;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Permission;

public interface PermissionService {

    Result<?> SavePermission(Permission permission);

    Result<?> UpdatePermission(Permission permission);

    Result<?> DeletePermissionById(Long id);

    Result<?> FindPermissionById(Long id);

    Result<?> FindAllPermission();

    Result<?> FindAllPermissionByPage(Integer pageNum, Integer pageSize, String search);
}
