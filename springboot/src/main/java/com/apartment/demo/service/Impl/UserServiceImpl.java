package com.apartment.demo.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Permission;
import com.apartment.demo.entity.User;
import com.apartment.demo.entity.UserRole;
import com.apartment.demo.enums.PwdEnum;
import com.apartment.demo.enums.RoleEnum;
import com.apartment.demo.mapper.PermissionMapper;
import com.apartment.demo.mapper.RoleMapper;
import com.apartment.demo.mapper.UserMapper;
import com.apartment.demo.mapper.UserRoleMapper;
import com.apartment.demo.service.BaseService;
import com.apartment.demo.service.UserService;
import com.apartment.demo.utils.TokenUtils;
import com.apartment.demo.utils.IDCardUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;
    @Resource
    RoleMapper roleMapper;
    @Resource
    PermissionMapper permissionMapper;
    @Resource
    UserRoleMapper userRoleMapper;
    @Resource
    BaseService baseService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; //注入bcryct加密


    @Override
    public Result<?> LoginUser(User userParam) {
        User userPwd = userMapper.selectByName(userParam.getUsername());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userParam.getUsername());
        queryWrapper.eq("password", userPwd.getPassword());
        User res = userMapper.selectOne(queryWrapper);

        // 判断密码是否正确
//        if (!bCryptPasswordEncoder.matches(userParam.getPassword(), userPwd.getPassword())) {
//            return Result.error("-1", "密码错误");
//        }
        if (res == null) {
            return Result.error("-1", "用户名或密码错误");
        }
        HashSet<Permission> permissionSet = new HashSet<>();
        // 1. 从user_role表通过用户id查询所有的角色信息
        Integer userId = res.getId();
        List<UserRole> userRoles = roleMapper.getUserRoleByUserId(userId);
        // 设置角色id
        res.setRoles(userRoles.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList()));
        for (UserRole userRole : userRoles) {
            // 2.根据roleId从role_permission表查询出所有的permissionId
            List<Permission> permissions = permissionMapper.getRolePermissionByRoleId(userRole.getRoleId());
            for (Permission permission : permissions) {
                Integer permissionId = permission.getId();
                // 3. 根据permissionPid查询permission信息
                permission.setChildrenList(permissionMapper.getChildrenPermissionyPid(userRole.getRoleId(),permissionId));
                permissionSet.add(permission);
            }
        }
        // 对资源根据id进行排序
        LinkedHashSet<Permission> sortedSet = permissionSet.stream().sorted(Comparator.comparing(Permission::getId)).collect(Collectors.toCollection(LinkedHashSet::new));
        //设置当前用户的资源信息
        res.setPermissions(sortedSet);

        // 生成token
        String token = TokenUtils.genToken(res);
        res.setToken(token);
        return Result.success(res);
    }

    @Override
    public Result<?> Register(User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (res != null) {
            return Result.error("-1", "用户名重复");
        }
        User userInfo = User.builder()
                .username(user.getUsername())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .nickname("用户" + IdWorker.getId())
                .telephone(user.getTelephone())
                .build();

        userMapper.insert(userInfo);

        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(RoleEnum.PASSAGE.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
        return Result.success();
    }

    @Override
    public Result<?> SaveUser(User user) {
        if (user.getPassword() == null) {
            user.setPassword(bCryptPasswordEncoder.encode(PwdEnum.PASSWORD.getPassword()));
        }
        userMapper.insert(user);

        UserRole userRole = UserRole.builder()
                .userId(user.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
        return Result.success();
    }

    @Override
    public Result<?> UpdateUser(User user) {
        userMapper.updateById(user);
        return Result.success();
    }

    @Override
    public Result<?> ChangePwd(Map<String, Object> map) {
        User user = userMapper.selectById((Integer) map.get("userId"));
        if (user== null) {
            return Result.error("-1", "未找到用户");
        }
        if (!bCryptPasswordEncoder.matches(map.get("cpassword").toString(), user.getPassword())) {
            return Result.error("-1", "原密码错误");
        }
        map.put("newPass", (bCryptPasswordEncoder.encode(map.get("newPass").toString())));
        userMapper.updatePass(map);
        return Result.success();
    }

    @Override
    public Result<?> DeleteUser(Long id) {
        userRoleMapper.deleteByUserRoleId(id);
        userMapper.deleteById(id);
        return Result.success();
    }

    @Override
    public Result<?> ChangeRole(User user) {
        // 先根据角色id删除所有的角色跟权限的绑定关系
        roleMapper.deleteRoleByUserId(user.getId());
        // 再新增 新的绑定关系
        for (Integer roleId : user.getRoles()) {
            roleMapper.insertUserRole(user.getId(), roleId);
        }

        // 获取当前登录用户的角色id列表
        User currentUser = baseService.getUser();
        // 如果当前登录用户的角色列表包含需要修改的角色id，那么就重新登录
        if (user.getId().equals(currentUser.getId())) {
            return Result.success(true);
        }
        // 如果不包含，则返回false，不需要重新登录。
        return Result.success(false);
    }

    @Override
    public Result<?> FindUserById(Long id) {
        return Result.success(userMapper.selectById(id));
    }

    @Override
    public Result<?> FindAllUser() {
        return Result.success(userMapper.findAllUser(new Page<>(1, 100),null));
    }

    @Override
    public Result<?> FindAdmin() {
        return Result.success(userMapper.findAllAdmin());
    }

    @Override
    public Result<?> CountUser() {
        return Result.success(userMapper.countAddress());
    }

    @Override
    public Result<?> FindUserByPage(Integer pageNum, Integer pageSize, String search, Integer userId) {
        UserRole userRole = userRoleMapper.selectByUserId(userId);
        Page<User> userPage = null;
        if(userRole.getRoleId() == 1) {
            userPage = userMapper.findAllUser(new Page<>(pageNum, pageSize), search);
        } else {
            userPage = userMapper.findAllConcractUser(new Page<>(pageNum, pageSize), search, userId);
        }
        // 设置用户的角色id列表
        for (User record : userPage.getRecords()) {
            List<UserRole> roles = roleMapper.getUserRoleByUserId(record.getId());
            List<Integer> roleIds = roles.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList());
            record.setRoles(roleIds);
        }
        return Result.success(userPage);
    }

    @Override
    public List<Map<String, Object>> ExportUser(){
        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<User> all = userMapper.selectList(null);
        for (User user : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("用户名", user.getUsername());
            row1.put("昵称", user.getNickname());
            row1.put("年龄", user.getAge());
            row1.put("性别", user.getSex());
            row1.put("地址", user.getAddress());
            list.add(row1);
        }
        return list;
    }

    @Override
    public Result<?> UploadUser(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
        List<User> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setNickname(row.get(1).toString());
            user.setAge(Integer.valueOf(row.get(2).toString()));
            user.setSex(row.get(3).toString());
            user.setAddress(row.get(4).toString());
            saveList.add(user);
        }
        for (User user : saveList) {
            userMapper.insert(user);
        }
        return Result.success();
    }

    @Override
    public Result<?> MatchId(User user) {
//        if(IDCardUtil.isLegalPattern(user.getIdcode())){
        if(true){
            userMapper.updateById(user);
            //更改用户权限为普通用户
            UserRole userRole = userRoleMapper.selectByUserId(user.getId());
            userRole.setRoleId(RoleEnum.USER.getRoleId());
            userRoleMapper.updateByUserId(userRole);
            return Result.success();
        }
        return Result.error("-1","身份证号有误，请重新输入");
    }
}
