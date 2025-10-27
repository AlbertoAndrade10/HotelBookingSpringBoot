package main.java.com.porfolio.hotel_service.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@Data
public class Hotel {


    @Id
    @GeneratedValue(strtrategy=GenerationType.IDENTY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> rooms;
    
    
}
