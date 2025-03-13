package abbtech.finalproject.HotelService.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String checkInDate;

    @Column(nullable = false)
    private String checkOutDate;

    @Column(nullable = false)
    private String comfirmationCode;

    public void setConfirmationCode(String abc123) {

    }
}