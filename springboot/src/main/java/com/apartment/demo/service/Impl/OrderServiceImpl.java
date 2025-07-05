package com.apartment.demo.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.apartment.demo.common.Result;
import com.apartment.demo.entity.*;
import com.apartment.demo.mapper.ApartmentMapper;
import com.apartment.demo.mapper.OrderMapper;
import com.apartment.demo.mapper.RoomMapper;
import com.apartment.demo.mapper.UserRoleMapper;
import com.apartment.demo.service.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderMapper orderMapper;
    @Resource
    UserRoleMapper userRoleMapper;
    @Resource
    RoomMapper roomMapper;
    @Resource
    ApartmentMapper apartmentMapper;


    @Override
    public Result<?> SaveOrder(Order order) {
        orderMapper.insert(order);
        return Result.success();
    }

    @Override
    public Result<?> UpdateOrder(Order order) {
        orderMapper.updateById(order);
        return Result.success();
    }

    @Override
    public Result<?> DeleteOrderById(Long id) {
        orderMapper.deleteById(id);
        return Result.success();
    }

    @Override
    public Result<?> FindOrderById(Long id) {
        return Result.success(orderMapper.selectById(id));
    }

    @Override
    public String BuyRoom(User user,Integer roomId,Long diffDays) {
        Room room = roomMapper.selectById(roomId);
        String orderNo = IdUtil.getSnowflake().nextIdStr();
        BigDecimal account =  room.getPrice().multiply(new BigDecimal(diffDays));
        Apartment apartment = apartmentMapper.findApartmentByRoomId(roomId);
        Order order = new Order();
        order.setName(apartment.getName()+room.getName()+"房间"+diffDays+"天房间租金");
        order.setOrderNo(orderNo);
        order.setTotalPrice(account);
        order.setPayPrice(account);
        order.setAdminId(apartment.getAdminId());
        String payUrl = "http://localhost:9090/alipay/pay?subject="
                + order.getName() + "&traceNo="
                + order.getOrderNo() + "&totalAmount="
                + order.getTotalPrice();

        order.setUserId(user.getId());
        order.setUsername(user.getTruename());

        orderMapper.insert(order);
        // 新建订单，扣减库存
        return payUrl;
    }

    @Override
    public Result<?> FindAllOrderByPage(Integer pageNum, Integer pageSize, String search, String state, Integer userId) {
        UserRole userRole = userRoleMapper.selectByUserId(userId);
        //判断系统管理员和酒店管理员
        Page<Order> orderPage = null;
        if(userRole.getRoleId()==1){
            orderPage = orderMapper.findAllOrder(new Page<>(pageNum, pageSize), search, state,null,null);
        } else if(userRole.getRoleId()==4){
            orderPage = orderMapper.findAllOrder(new Page<>(pageNum, pageSize), search, state, null,userId);
        } else {
            orderPage = orderMapper.findAllOrder(new Page<>(pageNum, pageSize), search, state, userId,null);
        }
        return Result.success(orderPage);
    }

    @Override
    public Result<?> Buy(Order order) {
        String payUrl = "http://localhost:9090/alipay/pay?subject="
                + order.getName() + "&traceNo="
                + order.getOrderNo() + "&totalAmount="
                + order.getTotalPrice();
        return Result.success(payUrl);
    }

    @Override
    public Result<?> OrdersContract() {
        return Result.success(orderMapper.countOrders());
    }
}
