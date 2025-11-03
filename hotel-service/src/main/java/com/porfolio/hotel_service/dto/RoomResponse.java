package com.porfolio.hotel_service.dto;

import com.porfolio.hotel_service.entity.Room;

import java.math.BigDecimal;


import lombok.Data;

@Data
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private String type;
    private BigDecimal pricePerNight;
    private Long hotelId;

    public RoomResponse(Room room) {

        this.id = room.getId();
        this.roomNumber = room.getRoomNumber();
        this.type = room.getType();
        this.pricePerNight = room.getPricePerNight();

        if (room.getHotel() != null) {
            this.hotelId = room.getHotel().getId();
        }
    }
}
