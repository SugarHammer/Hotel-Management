package com.apartment.demo.service.Impl;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.*;
import com.apartment.demo.enums.RoleEnum;
import com.apartment.demo.mapper.*;
import com.apartment.demo.service.ContractService;
import com.apartment.demo.service.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContractServiceImpl implements ContractService {

    @Resource
    private ContractMapper contractMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ApartmentMapper apartmentMapper;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private OrderService orderService;

    @Override
    public Result<?> alluser() {
        return Result.success(userMapper.findAllUserNoContract());
    }

    @Override
    public Result<?> allroom(Integer adminId) {
        UserRole userRole = userRoleMapper.selectByUserId(adminId);
        //判断系统管理员和酒店管理员
        List<Room> orderPage = null;
        if (userRole.getRoleId() == 1) {
            orderPage = roomMapper.findEmptyRoom(null);
        } else {
            orderPage = roomMapper.findEmptyRoom(adminId);
        }
        return Result.success(orderPage);
    }

    @Override
    public Result<?> addContract(Contract contract) {
        //插入所属酒店id
        contract.setApartmentId(roomMapper.findRoomByRoomId(contract.getRoomId()).getApartmentId());
        contractMapper.insert(contract);
        Room room = roomMapper.findRoomByRoomId(contract.getRoomId());
        long diffDays = (contract.getTerminTime().getTime() - contract.getCreateTime().getTime()) / (24 * 60 * 60 * 1000);
        User user = userMapper.selectById(contract.getUserId());
        //创建购买链接
        String payUrl = orderService.BuyRoom(user,room.getId(),diffDays);
        room.setHasPeople("是");
        //更改用户权限为已签约用户
        UserRole userRole = userRoleMapper.selectByUserId(contract.getUserId());
        userRole.setRoleId(RoleEnum.CONTRACTUSER.getRoleId());
        userRoleMapper.updateByUserId(userRole);
        roomMapper.updateById(room);
        return Result.success(payUrl);
    }

    @Override
    public Result<?> findAllContract(Integer pageNum, Integer pageSize, String search,Integer userId) {
        UserRole userRole = userRoleMapper.selectByUserId(userId);
        //判断系统管理员和酒店管理员
        Page<Contract> orderPage = null;
        if (userRole.getRoleId() == 1) {
            orderPage = contractMapper.findAllContract(new Page<>(pageNum, pageSize),search,null);
        } else {
            orderPage = contractMapper.findAllContract(new Page<>(pageNum, pageSize),search,userId);
        }
        return Result.success(orderPage);
    }

    @Override
    public Result<?> findself(Integer userId) {
        return Result.success(contractMapper.findContractByUserId(userId));
    }

    @Override
    public Result<?> leaveApartment(Integer conId) {
        Contract contract = contractMapper.selectById(conId);
        //更改用户权限为普通用户
        UserRole userRole = userRoleMapper.selectByUserId(contract.getUserId());
        userRole.setRoleId(RoleEnum.USER.getRoleId());
        userRoleMapper.updateByUserId(userRole);
        contractMapper.deleteById(contract.getConId());
        return Result.success();
    }

    @Override
    public Result<?> findselfHasPic(Integer id) {
        List list = new ArrayList();
        Contract contract = contractMapper.findContractByUserId(id);
        list.add(contract);
        list.add(roomMapper.findRoomByRoomId(contract.getRoomId()));
        return Result.success(list);
    }

    @Override
    public Map<String, Object> download(Long id) {
        Contract contract = contractMapper.selectById(id);
        User user = userMapper.selectById(contract.getUserId());
        User admin = userMapper.selectById(contract.getAdminId());
        Apartment apartment = apartmentMapper.selectById(contract.getApartmentId());
        Room room = roomMapper.selectById(contract.getRoomId());
        Map<String,Object> params = new HashMap<>();
        params.put("username",user.getTruename());
        params.put("adminname",admin.getTruename());
        params.put("address",apartment.getAddress());
        params.put("roomname",apartment.getName()+room.getName()+"室");
        params.put("useridcode",user.getIdcode());
        params.put("adminidcode",admin.getIdcode());
        params.put("usertelephone",user.getTelephone());
        params.put("admintelephone",admin.getTelephone());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        params.put("createtime",sdf.format(contract.getCreateTime()));
        params.put("termintime",sdf.format(contract.getTerminTime()));
        params.put("roomprice",room.getPrice().toString());
        return params;
    }

    @Override
    public Result<?> CountContract(){
        return Result.success(contractMapper.countContract());
    }
}
