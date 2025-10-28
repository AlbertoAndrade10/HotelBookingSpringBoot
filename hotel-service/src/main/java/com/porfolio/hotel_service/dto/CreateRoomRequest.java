package com.porfolio.hotel_service.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreateRoomRequest {

    private String roomNumber;
    private String type;
    private BigDecimal pricePerNight;
    private Long hotelId;

    public CreateRoomRequest() {
    }

    public CreateRoomRequest(String roomNumber, String type, BigDecimal pricePerNight, Long hotelId) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.hotelId = hotelId;
    }
}
