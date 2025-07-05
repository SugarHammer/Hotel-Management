package com.apartment.demo.controller;


import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Message;
import com.apartment.demo.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/message")
public class MessageController extends BaseController{

    @Resource
    private MessageService messageService;

    @PostMapping
    public Result<?> save(@RequestBody Message message) {
       return messageService.SaveMessage(message);
    }

    @PutMapping
    public Result<?> update(@RequestBody Message message) {
        return messageService.UpdateMessage(message);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return messageService.DeleteMessageById(id);
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return messageService.FindMessageById(id);
    }

    @GetMapping
    public Result<?> findAll() {
        return messageService.FindAllMessage();
    }

    // 查询所有数据
    @GetMapping("/foreign/{id}")
    public Result<?> foreign(@PathVariable Integer id) {
        return messageService.FindForeign(id);
    }

    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(required = false, defaultValue = "") String search,
                              @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return messageService.FindAllMessageByPage(pageNum, pageSize, search);
    }



}
