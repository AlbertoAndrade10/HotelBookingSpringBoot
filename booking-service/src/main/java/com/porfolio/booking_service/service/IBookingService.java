package com.porfolio.booking_service.service;

import java.time.LocalDateTime;
import java.util.List;

import com.porfolio.booking_service.entity.Booking;
import com.porfolio.booking_service.entity.BookingStatus;

public interface IBookingService {

    Booking createBooking(Long userId, Long roomId, LocalDateTime checkIn, LocalDateTime checkOut);

    List<Booking> getAllBookings();

    Booking getBookingById(Long id);

    List<Booking> getBookingByUserId(Long userId);

    List<Booking> getBookingByStatus(BookingStatus status);
}
