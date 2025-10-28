package com.porfolio.hotel_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.porfolio.hotel_service.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotel_Id(Long hotelId);
}
