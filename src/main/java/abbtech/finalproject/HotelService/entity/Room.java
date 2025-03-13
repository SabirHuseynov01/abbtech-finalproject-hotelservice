package abbtech.finalproject.HotelService.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rooms")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean available;

    @ManyToOne
    @JoinColumn (name = "hotel_id", nullable = false)
    private Hotel hotel;
}
