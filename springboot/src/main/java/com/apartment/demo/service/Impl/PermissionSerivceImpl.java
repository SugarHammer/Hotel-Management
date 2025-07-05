package com.apartment.demo.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Permission;
import com.apartment.demo.mapper.PermissionMapper;
import com.apartment.demo.service.PermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PermissionSerivceImpl implements PermissionService {

    @Resource
    PermissionMapper PermissionMapper;

    @Override
    public Result<?> SavePermission(Permission permission) {
        PermissionMapper.insert(permission);
        return Result.success();
    }

    @Override
    public Result<?> UpdatePermission(Permission permission) {
        PermissionMapper.updateById(permission);
        return Result.success();
    }

    @Override
    public Result<?> DeletePermissionById(Long id) {
        PermissionMapper.deleteById(id);
        return Result.success();
    }

    @Override
    public Result<?> FindPermissionById(Long id) {
        return Result.success(PermissionMapper.findById(id));
    }

    @Override
    public Result<?> FindAllPermission() {
        return Result.success(PermissionMapper.selectAll());
    }

    @Override
    public Result<?> FindAllPermissionByPage(Integer pageNum, Integer pageSize, String search) {
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Permission::getName, search);
        }
        Page<Permission> PermissionPage = PermissionMapper.selectAllPage(new Page<>(pageNum, pageSize));
        return Result.success(PermissionPage);
    }
}
