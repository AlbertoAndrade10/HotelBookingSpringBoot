package com.porfolio.booking_service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CreateBookingRequest {
    private Long userId;
    private Long roomId;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
}
