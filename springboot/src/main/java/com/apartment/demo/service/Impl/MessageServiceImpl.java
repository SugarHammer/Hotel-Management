package com.apartment.demo.service.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Message;
import com.apartment.demo.entity.User;
import com.apartment.demo.mapper.MessageMapper;
import com.apartment.demo.mapper.UserMapper;
import com.apartment.demo.service.BaseService;
import com.apartment.demo.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    BaseService baseService;
    @Resource
    HttpServletRequest request;

    @Override
    public Result<?> SaveMessage(Message message) {
        message.setUsername(baseService.getUser().getUsername());
        message.setTime(DateUtil.formatDateTime(new Date()));
        return Result.success(messageMapper.insert(message));
    }

    @Override
    public Result<?> UpdateMessage(Message message) {
        return Result.success(messageMapper.updateById(message));
    }

    @Override
    public Result<?> DeleteMessageById(Long id) {
        messageMapper.deleteById(id);
        return Result.success();
    }

    @Override
    public Result<?> FindMessageById(Long id) {
        return Result.success(messageMapper.selectById(id));
    }

    @Override
    public Result<?> FindAllMessage() {
        return Result.success(messageMapper.selectList(null));
    }

    @Override
    public Result<?> FindAllMessageByPage(Integer pageNum, Integer pageSize, String search) {
        LambdaQueryWrapper<Message> query = Wrappers.<Message>lambdaQuery().like(Message::getContent, search).orderByDesc(Message::getId);
        return Result.success(messageMapper.selectPage(new Page<>(pageNum, pageSize), query));

    }

    @Override
    public Result<?> FindForeign(Integer id) {
        return Result.success(findByForeign(id));
    }

    private List<Message> findByForeign(Integer foreignId) {
        // 根据 foreignId 0 查询所有的留言数据
        LambdaQueryWrapper<Message> queryWrapper = Wrappers.<Message>lambdaQuery().eq(Message::getForeignId, foreignId).orderByDesc(Message::getId);
        List<Message> list = messageMapper.selectList(queryWrapper);
        // 循环所有留言数据
        for (Message Message : list) {
            User one = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, Message.getUsername()));
            if (StrUtil.isNotBlank(one.getAvatar())) {
                Message.setAvatar(one.getAvatar());
            } else {
                // 默认一个头像
                Message.setAvatar("https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png");
            }
            Long parentId = Message.getParentId();
            // 判断当前的留言是否有父级，如果有，则返回父级留言的信息
            // 原理：遍历所有留言数据，如果id跟当前留言信息的parentId相等，则将其设置为父级评论信息，也就是Message::setParentMessage
            list.stream().filter(c -> c.getId().equals(parentId)).findFirst().ifPresent(Message::setParentMessage);
        }
        return list;
    }
}
