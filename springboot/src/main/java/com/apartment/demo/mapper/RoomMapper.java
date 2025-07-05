package com.apartment.demo.mapper;

import com.apartment.demo.entity.Apartment;
import com.apartment.demo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.apartment.demo.entity.Room;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface RoomMapper extends BaseMapper<Room>{

    Page<Room> findAllRoom(Page page, @Param("name") String name,
                                      @Param("adminId")  Integer adminId,
                                      @Param("apartmentId")  Integer apartmentId,
                                      @Param("hasPeople") String hasPeople);

    List<Room> findEmptyRoom(@Param("adminId") Integer adminId);

    List<Room> allEngineerRoom(@Param("adminId")Integer adminId);

    Room findRoomByRoomId(@Param("roomId")Integer id);

    List<Room> findRoomByRandom();

    @Update("update room set password = #{newPass} where id = #{roomId}")
    int updatePass(Map<String, Object> map);

    @Select("select * from apartment where admin_id = #{currentUserId}")
    Apartment selectApartmentBycurrentUserID(@Param("currentUserId")Integer id);
}
