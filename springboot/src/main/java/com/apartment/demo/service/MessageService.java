package com.apartment.demo.service;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Message;

public interface MessageService {

    Result<?> SaveMessage(Message message);

    Result<?> UpdateMessage(Message message);

    Result<?> DeleteMessageById(Long id);

    Result<?> FindMessageById(Long id);

    Result<?> FindAllMessage();

    Result<?> FindAllMessageByPage(Integer pageNum, Integer pageSize, String search);

    Result<?> FindForeign(Integer id);
}
