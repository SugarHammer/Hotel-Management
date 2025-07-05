package com.apartment.demo.controller;

import com.apartment.demo.service.OrderService;
import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Order;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Resource
    OrderService orderService;

    @PostMapping
    public Result<?> save(@RequestBody Order order) {
        return orderService.SaveOrder(order);
    }

    @PutMapping
    public Result<?> update(@RequestBody Order order) {
        return orderService.UpdateOrder(order);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return orderService.DeleteOrderById(id);
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return orderService.FindOrderById(id);
    }

//    @GetMapping("/buyRoom/{roomId}")
//    public Result<?> buy(@PathVariable Integer roomId) {
//        return orderService.BuyRoom(roomId);
//    }

    @PostMapping("/buy")
    public Result<?> buy(@RequestBody Order order){
        return orderService.Buy(order);
    }

    /**
     * 统计数据
     *
     * @return
     */
    @GetMapping("/count")
    public Result<?> count() {
        return orderService.OrdersContract();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "0") String state,
                              @RequestParam(defaultValue = "") Integer userId) {
        return orderService.FindAllOrderByPage(pageNum, pageSize, search, state, userId);
    }
}
