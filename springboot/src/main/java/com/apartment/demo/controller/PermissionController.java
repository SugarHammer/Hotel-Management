package com.apartment.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.apartment.demo.common.Result;
import com.apartment.demo.service.PermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.apartment.demo.entity.Permission;
import com.apartment.demo.mapper.PermissionMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController {

    @Resource
    PermissionService permissionService;

    @PostMapping
    public Result<?> save(@RequestBody Permission permission) {
        return permissionService.SavePermission(permission);
    }

    @PutMapping
    public Result<?> update(@RequestBody Permission permission) {
        return permissionService.UpdatePermission(permission);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return permissionService.DeletePermissionById(id);
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return permissionService.FindPermissionById(id);
    }

    @GetMapping("/all")
    public Result<?> all() {
        return permissionService.FindAllPermission();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        return permissionService.FindAllPermissionByPage(pageNum, pageSize, search);
    }
}
