package main.java.com.porfolio.hotel_service.controller;

import com.porfolio.hotelservice.entity.Room;
import com.porfolio.hotelservice.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomRepository.findById(id);
        return room.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody main.java.com.porfolio.hotel_service.entity.Room room) {

        Room savedRoom = roomRepository.save(room);

        if (!hotelRepository.existsById(room.getHotelId())) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(savedRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room roomDetails) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            Room r = room.get();
            r.setRoomNumber(roomDetails.getRoomNumber());
            r.setType(roomDetails.getType());
            r.setPricePerNight(roomDetails.getPricePerNight());
            // No se actualiza el hotel aqui directamente, se hace desde HotelController
            Room updatedRoom = roomRepository.save(r);
            return ResponseEntity.ok(updatedRoom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}