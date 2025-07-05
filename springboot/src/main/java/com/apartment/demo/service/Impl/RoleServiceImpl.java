package com.apartment.demo.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Permission;
import com.apartment.demo.entity.Role;
import com.apartment.demo.entity.User;
import com.apartment.demo.entity.UserRole;
import com.apartment.demo.mapper.PermissionMapper;
import com.apartment.demo.mapper.RoleMapper;
import com.apartment.demo.mapper.UserRoleMapper;
import com.apartment.demo.service.BaseService;
import com.apartment.demo.service.RoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    RoleMapper roleMapper;
    @Resource
    PermissionMapper permissionMapper;
    @Resource
    BaseService baseService;

    @Override
    public Result<?> SaveRole(Role Role) {
        roleMapper.insert(Role);
        return Result.success();
    }

    @Override
    public Result<?> UpdateRole(Role Role) {
        roleMapper.updateById(Role);
        return Result.success();
    }

    @Override
    public Result<?> ChangeRolePermission(Role role) {
        // 先根据角色id删除所有的角色跟权限的绑定关系
        permissionMapper.deletePermissionsByRoleId(role.getId());
        // 再新增 新的绑定关系
        for (Integer permissionId : role.getPermissions()) {
            permissionMapper.insertRoleAndPermission(role.getId(), permissionId);
        }

        // 判断当前登录的用户角色是否包含了当前操作行的角色id，如果包含，则返回true，需要重新登录。
        User user = baseService.getUser();
        List<UserRole> userRoles = roleMapper.getUserRoleByUserId(user.getId());
        if (userRoles.stream().anyMatch(userRole -> userRole.getRoleId().equals(role.getId()))) {
            return Result.success(true);
        }
//        如果不包含，则返回false，不需要重新登录。
        return Result.success(false);
    }

    @Override
    public Result<?> DeleteRoleById(Long id) {
        roleMapper.deleteById(id);
        return Result.success();
    }

    @Override
    public Result<?> FindRoleById(Long id) {
        return Result.success(roleMapper.selectById(id));
    }

    @Override
    public Result<?> FindAllRole() {
        return Result.success(roleMapper.selectList(null));
    }

    @Override
    public Result<?> FindAllRoleByPage(Integer pageNum, Integer pageSize, String search) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Role::getName, search);
        }
        Page<Role> rolePage = roleMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        List<Role> records = rolePage.getRecords();
        // 给角色设置绑定的权限id数组
        for (Role record : records) {
            Integer roleId = record.getId();
            List<Integer> permissions = permissionMapper.getRoleChangePermissionByRoleId(roleId).stream().map(Permission::getId).collect(Collectors.toList());
            record.setPermissions(permissions);
        }
        return Result.success(rolePage);
    }

}
