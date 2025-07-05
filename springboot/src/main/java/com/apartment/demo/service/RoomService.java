package com.apartment.demo.service;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface RoomService {

    Result<?> SaveRoom(Room room);

    Result<?> UpdateRoom(Room room);

    Result<?> DeleteRoom(Long id);

    Result<?> FindAllRoom(Integer pageNum, Integer pageSize, String search,Integer adminId,Integer apartmentId,String hasPeople);

    Result<?> FindRoomByRandom();

    List<Map<String, Object>> ExportRoom() throws IOException;

    Result<?> FindRoomByRoomId(Integer id);

    Result<?> UploadRoom(MultipartFile file) throws IOException;

    Result<?> ChangePwd(Map<String, Object> map);
}
