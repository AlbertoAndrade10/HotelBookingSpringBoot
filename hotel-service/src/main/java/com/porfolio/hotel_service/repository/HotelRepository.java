package main.java.com.porfolio.hotel_service.repository;

import com.porfolio.hotelservice.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelRepository extends JpaRepository<Hotel, Long> {


    
}
