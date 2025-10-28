package com.porfolio.hotel_service.controller;

import com.porfolio.hotel_service.dto.CreateRoomRequest;
import com.porfolio.hotel_service.dto.RoomResponse;
import com.porfolio.hotel_service.entity.Hotel;
import com.porfolio.hotel_service.entity.Room;
import com.porfolio.hotel_service.repository.HotelRepository;
import com.porfolio.hotel_service.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponse> roomResponses = rooms.stream()
                .map(RoomResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roomResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            RoomResponse roomResponse = new RoomResponse(roomOpt.get());
            return ResponseEntity.ok(roomResponse);
        } else {
            return roomOpt.map(RoomResponse::new).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }

    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody CreateRoomRequest request) {

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + request.getHotelId()));

        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setType(request.getType());
        room.setPricePerNight(request.getPricePerNight());
        room.setHotel(hotel);

        Room savedRoom = roomRepository.save(room);
        return ResponseEntity.ok(savedRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @RequestBody Room roomDetails) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            room.setRoomNumber(roomDetails.getRoomNumber());
            room.setType(roomDetails.getType());
            room.setPricePerNight(roomDetails.getPricePerNight());

            if (roomDetails.getHotelId() != null) {
                Hotel hotel = hotelRepository.findById(roomDetails.getHotelId())
                        .orElseThrow(
                                () -> new RuntimeException("Hotel not found with id: " + roomDetails.getHotelId()));
                room.setHotel(hotel);
            }
            Room updatedRoomEntity = roomRepository.save(room);
            RoomResponse updatedRoomResponse = new RoomResponse(updatedRoomEntity);
            return ResponseEntity.ok(updatedRoomResponse);
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