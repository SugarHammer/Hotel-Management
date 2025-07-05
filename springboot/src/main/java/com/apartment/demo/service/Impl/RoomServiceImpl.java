package com.apartment.demo.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Apartment;
import com.apartment.demo.entity.Room;
import com.apartment.demo.entity.User;
import com.apartment.demo.entity.UserRole;
import com.apartment.demo.enums.PwdEnum;
import com.apartment.demo.mapper.RoomMapper;
import com.apartment.demo.mapper.UserRoleMapper;
import com.apartment.demo.service.BaseService;
import com.apartment.demo.service.RoomService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomMapper roomMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    BaseService baseService;

    @Override
    public Result<?> SaveRoom(Room room) {
        if(room.getPassword() != null){
            room.setPassword(room.getPassword());
        } else {
            //默认密码
            room.setPassword(PwdEnum.ROOMPASSWORD.getPassword());
        }
        User currentUser = baseService.getUser();
        if (currentUser.getId()!=1){
            Apartment apartment = roomMapper.selectApartmentBycurrentUserID(currentUser.getId());
            room.setApartmentId(apartment.getId());
        }
        room.setHasPeople("否");
        roomMapper.insert(room);
        return Result.success();
    }

    @Override
    public Result<?> UpdateRoom(Room room) {
        if(room.getPassword() != null){
            room.setPassword(room.getPassword());
        }
        roomMapper.updateById(room);
        return Result.success();
    }

    @Override
    public Result<?> DeleteRoom(Long id) {
        roomMapper.deleteById(id);
        return Result.success();
    }

    @Override
    public Result<?> FindAllRoom(Integer pageNum, Integer pageSize, String search, Integer userId, Integer apartmentId, String hasPeople) {
        UserRole userRole = userRoleMapper.selectByUserId(userId);
        //判断系统管理员和酒店管理员
        Page<Room> orderPage = null;
        if(userRole.getRoleId()==1){
            orderPage = roomMapper.findAllRoom(new Page<>(pageNum, pageSize),search,null,apartmentId,hasPeople);
        } else {
            orderPage = roomMapper.findAllRoom(new Page<>(pageNum, pageSize),search,userId,null,hasPeople);
        }
        return Result.success(orderPage);
    }

    public Result<?> FindRoomByRandom(){
        return Result.success(roomMapper.findRoomByRandom());
    }


    @Override
    public List<Map<String, Object>> ExportRoom() throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Room> all = roomMapper.selectList(null);
        for (Room room : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("房间名", room.getName());
            row1.put("是否有人入住", room.getHasPeople());
            row1.put("所属酒店ID", room.getApartmentId());
            row1.put("价格", room.getPrice());
            row1.put("房间密码", room.getPassword());
            list.add(row1);
        }
        return list;
    }

    @Override
    public Result<?> FindRoomByRoomId(Integer id) {
        return Result.success(roomMapper.findRoomByRoomId(id));
    }

    @Override
    public Result<?> UploadRoom(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
        List<Room> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Room room = new Room();
            room.setName(row.get(0).toString());
            room.setHasPeople(row.get(1).toString());
            room.setApartmentId(Integer.valueOf(row.get(2).toString()));
            room.setPrice(BigDecimal.valueOf(Double.valueOf(row.get(3).toString())));
            room.setPassword(row.get(4).toString());
            room.setApartmentName(row.get(5).toString());
            saveList.add(room);
        }
        for (Room room : saveList) {
            roomMapper.insert(room);
        }
        return Result.success();
    }

    @Override
    public Result<?> ChangePwd(Map<String, Object> map) {
        Room room = roomMapper.selectById((Integer) map.get("roomId"));
        if (room == null) {
            return Result.error("-1", "未找到用户");
        }
        if (!map.get("cpassword").toString().equals(room.getPassword())) {
            return Result.error("-1", "原密码错误");
        }
        map.put("newPass", map.get("newPass").toString());
        roomMapper.updatePass(map);
        return Result.success();
    }

}
