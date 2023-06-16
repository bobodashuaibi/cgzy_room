package com.cgzy.mapper;

import com.cgzy.entity.t_Hotel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Hotel {

    void insertHotel(t_Hotel hotel);

    List<t_Hotel> selectHotelInfo(t_Hotel hotel);

    List<t_Hotel> selectHotelInfoByHotel(t_Hotel hotel);

    /**
     * 更新寝室目前住的人数
     */
    void updateRoomPeople(@Param("people_num") int people_num, @Param("hotel_number") String hotel_number, @Param("room_number") String room_number);

    /**
     * 管理员查询所有的寝室信息
     * @return
     */
    List<t_Hotel> hotelAllInfo();

    /**
     * 管理员删除指定寝室信息
     * @return
     */
    void delhotelInfo(t_Hotel t_hotel);


}
