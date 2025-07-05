package com.apartment.demo.service.Impl;

import com.apartment.demo.common.Result;
import com.apartment.demo.controller.dto.EngineerCountDto;
import com.apartment.demo.entity.Engineer;
import com.apartment.demo.entity.Room;
import com.apartment.demo.entity.UserRole;
import com.apartment.demo.mapper.*;
import com.apartment.demo.service.EngineerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class EngineerServiceImpl implements EngineerService {

    @Resource
    private EngineerMapper engineerMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private ApartmentMapper apartmentMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private ContractMapper contractMapper;


    @Override
    public Result<?> allEngineer() {
        return Result.success(userMapper.findAllEngineer());
    }

    @Override
    public Result<?> allEngineerRoom(Integer adminId) {
        UserRole userRole = userRoleMapper.selectByUserId(adminId);
        //判断系统管理员和酒店管理员
        List<Room> engineerPage = null;
        if (userRole.getRoleId() == 1) {
            engineerPage = roomMapper.allEngineerRoom(null);
        } else if(userRole.getRoleId() == 5){
            contractMapper.findContractByUserId(adminId);
            engineerPage = roomMapper.allEngineerRoom(adminId);
        } else {
            engineerPage = roomMapper.allEngineerRoom(adminId);
        }
        return Result.success(engineerPage);
    }

    @Override
    public Result<?> addEngineer(Engineer engineer) {
        engineer.setRoomId(contractMapper.findContractByUserId(engineer.getUserId()).getRoomId());
        engineer.setApartmentId(roomMapper.findRoomByRoomId(engineer.getRoomId()).getApartmentId());
        engineer.setAdminId(apartmentMapper.findApartmentByRoomId(engineer.getRoomId()).getAdminId());
        engineer.setCreateTime(new Date());
        engineer.setState(0);
        engineerMapper.insert(engineer);
        return Result.success();
    }

    @Override
    public Result<?> updateEngineer(Engineer engineer) {
        engineerMapper.updateById(engineer);
        return Result.success();
    }

    @Override
    public Result<?> findAllEngineerList(Integer pageNum, Integer pageSize, String state, Integer userId, String search) {
        UserRole userRole = userRoleMapper.selectByUserId(userId);
        //判断系统管理员和酒店管理员
        Page<Engineer> engineerPage = null;
        if(userRole.getRoleId()==1){
            engineerPage = engineerMapper.findAllEngineerList(new Page<>(pageNum, pageSize), null,null,null,state,search);
        } else if(userRole.getRoleId()==4){
            engineerPage = engineerMapper.findAllEngineerList(new Page<>(pageNum, pageSize),  null,userId,null,state,search);
        } else if(userRole.getRoleId()==3){
            engineerPage = engineerMapper.findAllEngineerList(new Page<>(pageNum, pageSize),  null,null,userId,state,search);
        } else {
            engineerPage = engineerMapper.findAllEngineerList(new Page<>(pageNum, pageSize), userId,null,null,state,search);
        }
        for (Engineer e: engineerPage.getRecords()) {
            e.setAdminName(userMapper.selectById(e.getAdminId()).getTruename());
            e.setRoomName(roomMapper.selectById(e.getRoomId()).getName());
            e.setApartmentName(apartmentMapper.selectById(e.getApartmentId()).getName());
            e.setUserName(userMapper.selectById(e.getUserId()).getTruename());
            if(!(e.getEngineerId()==null)){
                e.setEngineerName(userMapper.selectById(e.getEngineerId()).getNickname());
            }

        }
        return Result.success(engineerPage);
    }

    @Override
    public Result<?> findself(Integer userId) {
        return Result.success(engineerMapper.findself(userId));
    }

    @Override
    public Result<?> CountState() {
        List<EngineerCountDto> list = engineerMapper.countState();
        for (EngineerCountDto e:list) {
            if(e.getState().equals("0")){
                e.setState("未分配维修员");
            } else if(e.getState().equals("1")){
                e.setState("维修员维修中");
            } else if(e.getState().equals("2")){
                e.setState("维修员已完成");
            }
        }
        return Result.success(list);
    }

}
