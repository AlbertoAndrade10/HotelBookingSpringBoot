package com.porfolio.booking_service.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.porfolio.booking_service.client.HotelServiceClient;
import com.porfolio.booking_service.client.UserServiceClient;
import com.porfolio.booking_service.entity.Booking;
import com.porfolio.booking_service.entity.BookingStatus;
import com.porfolio.booking_service.repository.BookingRepository;

import jakarta.transaction.Transactional;

@Service
public class BookingServiceImpl implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private HotelServiceClient hotelServiceClient;

    @Transactional
    @Override
    public Booking createBooking(Long userId, Long roomId, LocalDateTime checkIn, LocalDateTime checkOut) {

        // step 1. validate user
        var user = userServiceClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        // step 2. get room details
        var room = hotelServiceClient.getRoomById(roomId);
        if (room == null) {
            throw new RuntimeException("room not found with id: " + roomId);
        }

        // step 3. calculate total price
        long days = Duration.between(checkIn, checkOut).toDays();
        BigDecimal pricePerNight = room.pricePerNight();
        BigDecimal totalPrice = pricePerNight.multiply(BigDecimal.valueOf(days));

        // step 4. create reservation
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomId(roomId);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING); // default

        return bookingRepository.save(booking);

    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Override
    public List<Booking> getBookingByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> getBookingByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

}
