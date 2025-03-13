package abbtech.finalproject.HotelService.service.impl;

import abbtech.finalproject.HotelService.dto.request.HotelSearchRequest;
import abbtech.finalproject.HotelService.dto.response.HotelBookingResponse;
import abbtech.finalproject.HotelService.dto.response.HotelResponse;
import abbtech.finalproject.HotelService.entity.Booking;
import abbtech.finalproject.HotelService.entity.Hotel;
import abbtech.finalproject.HotelService.entity.Room;
import abbtech.finalproject.HotelService.exception.HotelNotFoundException;
import abbtech.finalproject.HotelService.kafka.producer.HotelEventProducer;
import abbtech.finalproject.HotelService.repository.BookingRepository;
import abbtech.finalproject.HotelService.repository.HotelRepository;
import abbtech.finalproject.HotelService.repository.RoomRepository;
import abbtech.finalproject.HotelService.service.HotelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class HotelServiceImpl implements HotelService {

    public HotelEventProducer hotelEventProducer;
    @Autowired
    public HotelRepository hotelRepository;

    @Autowired
    public RoomRepository roomRepository;

    @Autowired
    public BookingRepository bookingRepository;

    @Override
    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotel -> new HotelResponse(hotel.getId(),
                        hotel.getName(),
                        hotel.getLocation(),
                        hotel.getPricePerNight(),
                        hotel.isAllInclusive()))
                .collect(Collectors.toList());
    }


    @Override
    @Cacheable(value = "hotels", key = "#request.location")
    public List<HotelResponse> searchHotels(HotelSearchRequest request) {
        return hotelRepository.findAll().stream()
                .filter(hotel -> hotel.getLocation().equalsIgnoreCase(request.location()))
                .map((hotel -> new HotelResponse(hotel.getId(),
                        hotel.getName(),
                        hotel.getLocation(),
                        hotel.getPricePerNight(),
                        hotel.isAllInclusive())))
                .collect(Collectors.toList());
    }

    @Override
    public HotelBookingResponse bookHotel(Long roomId, String userEmail, String checkInDate, String checkOutDate) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->new HotelNotFoundException("Room not found with ID: " + roomId));
        if (!room.isAvailable()) {
            throw new HotelNotFoundException("Room" + roomId + "is not available!");
        }
        Hotel hotel = room.getHotel();
        Booking booking = new Booking();
        booking.setRoomId(roomId);
        booking.setUserEmail(userEmail);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setComfirmationCode(UUID.randomUUID().toString());

        room.setAvailable(false);
        roomRepository.save(room);
        bookingRepository.save(booking);

        return new HotelBookingResponse(booking.getId(), hotel.getName(), booking.getComfirmationCode());
    }
}
