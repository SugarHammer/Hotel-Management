package com.apartment.demo.service;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Order;
import com.apartment.demo.entity.User;

public interface OrderService {

    Result<?> SaveOrder(Order order);

    Result<?> UpdateOrder(Order order);

    Result<?> DeleteOrderById(Long id);

    Result<?> FindOrderById(Long id);

    String BuyRoom(User user, Integer roomId, Long diffDays);

    Result<?> FindAllOrderByPage(Integer pageNum, Integer pageSize, String search, String state, Integer userId);

    Result<?> Buy(Order order);

    Result<?> OrdersContract();
}
