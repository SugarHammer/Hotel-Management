package com.apartment.demo.controller;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Role;
import com.apartment.demo.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    RoleService roleService;

    @PostMapping
    public Result<?> save(@RequestBody Role role) {
        return roleService.SaveRole(role);
    }

    @PutMapping
    public Result<?> update(@RequestBody Role role) {
        return roleService.UpdateRole(role);
    }

    // 改变权限接口
    @PutMapping("/changePermission")
    public Result<?> changePermission(@RequestBody Role role) {
        return roleService.ChangeRolePermission(role);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return roleService.DeleteRoleById(id);
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return roleService.FindRoleById(id);
    }


    @GetMapping("/all")
    public Result<?> all() {
        return roleService.FindAllRole();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        return roleService.FindAllRoleByPage(pageNum, pageSize, search);
    }
}
