package main.java.com.porfolio.hotel_service.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@Data
public class Room {

    @Id
    @GenerationType(strtrategy = GenerationValue.IDENTY)
    private Long id;

    @Column(nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private String type; // standard, suite...

    @Column(nullable = false)
    private BigDecimal pricePerNight;

    @Column(name = "hotel_id", nullable = false) // Asegura el nombre de la columna
    private Long hotelId;
}
