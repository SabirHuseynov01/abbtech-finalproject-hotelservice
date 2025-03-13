package abbtech.finalproject.HotelService.repository;

import abbtech.finalproject.HotelService.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
}
