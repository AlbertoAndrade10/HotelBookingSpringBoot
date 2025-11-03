package java.com.porfolio.hotel_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.com.porfolio.hotel_service.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

}