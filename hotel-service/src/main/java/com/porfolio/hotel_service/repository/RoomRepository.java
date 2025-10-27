package main.java.com.porfolio.hotel_service.repository;
import com.porfolio.hotelservice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);
    
}
