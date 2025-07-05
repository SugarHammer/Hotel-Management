package com.apartment.demo.mapper;

import com.apartment.demo.controller.dto.OrderCountDto;
import com.apartment.demo.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {

    @Update("update orders set state = #{state},pay_time = #{payTime} where order_no = #{tradeNo}")
    int updateState(@Param("tradeNo") String tradeNo, @Param("state") int state, @Param("payTime") String payTime);

    Page<Order> findAllOrder(Page page, @Param("name") String name, @Param("state") String state, @Param("userId")  Integer userId, @Param("adminId")  Integer adminId);

    @Select("select sum(o.pay_price) count,a.`name`\n" +
            "    FROM orders o,apartment a\n" +
            "    WHERE o.admin_id = a.admin_id\n" +
            "    GROUP BY a.id")
    List<OrderCountDto> countOrders();
}
