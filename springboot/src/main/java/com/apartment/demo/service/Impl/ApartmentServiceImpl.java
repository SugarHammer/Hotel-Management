package com.apartment.demo.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Apartment;
import com.apartment.demo.entity.UserRole;
import com.apartment.demo.mapper.ApartmentMapper;
import com.apartment.demo.mapper.UserRoleMapper;
import com.apartment.demo.service.ApartmentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    @Resource
    private ApartmentMapper apartmentMapper;
    @Resource
    UserRoleMapper userRoleMapper;

    @Override
    public Result<?> SaveApartment(Apartment apartment) {
        apartmentMapper.insert(apartment);
        return Result.success();
    }

    @Override
    public Result<?> UpdateApartment(Apartment apartment) {
        apartmentMapper.updateById(apartment);
        return Result.success();
    }

    @Override
    public Result<?> DeleteApartment(Long id) {
        apartmentMapper.deleteById(id);
        return Result.success();
    }

    @Override
    public Result<?> FindAllApartment(Integer pageNum, Integer pageSize, String search, Integer adminId) {
        UserRole userRole = userRoleMapper.selectByUserId(adminId);
        //判断系统管理员和酒店管理员
        Page<Apartment> orderPage = null;
        if (userRole.getRoleId() == 1) {
            orderPage = apartmentMapper.findAllApartment(new Page<>(pageNum, pageSize),search,null);
        } else {
            orderPage = apartmentMapper.findAllApartment(new Page<>(pageNum, pageSize),search,adminId);
        }
        return Result.success(orderPage);
    }

    @Override
    public Result<?> FindAllApartmentList() {
        return Result.success(apartmentMapper.findAllApartmentList());
    }

    @Override
    public Result<?> FindAllApartmentById(Integer adminId){
        return Result.success(apartmentMapper.findApartmentByAdminId(adminId));
    }

    public Result<?> FindApartmentByRandom(){
        return Result.success(apartmentMapper.findApartmentByRandom());
    }

    @Override
    public List<Map<String, Object>> ExportApartment() throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<Apartment> all = apartmentMapper.selectList(null);
        for (Apartment apartment : all) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("酒店名", apartment.getName());
            row1.put("地址", apartment.getAddress());
            row1.put("所属酒店管理员ID", apartment.getAdminId());
            list.add(row1);
        }
        return list;
    }

    @Override
    public Result<?> UploadApartment(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<List<Object>> lists = ExcelUtil.getReader(inputStream).read(1);
        List<Apartment> saveList = new ArrayList<>();
        for (List<Object> row : lists) {
            Apartment apartment = new Apartment();
            apartment.setName(row.get(0).toString());
            apartment.setAddress(row.get(1).toString());
            apartment.setAdminId(Integer.valueOf(row.get(2).toString()));
            saveList.add(apartment);
        }
        for (Apartment Apartment : saveList) {
            apartmentMapper.insert(Apartment);
        }
        return Result.success();
    }
}
