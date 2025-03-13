package abbtech.finalproject.HotelService.service;

import abbtech.finalproject.HotelService.dto.request.HotelSearchRequest;
import abbtech.finalproject.HotelService.dto.response.HotelBookingResponse;
import abbtech.finalproject.HotelService.dto.response.HotelResponse;
import abbtech.finalproject.HotelService.entity.Booking;
import abbtech.finalproject.HotelService.entity.Hotel;
import abbtech.finalproject.HotelService.entity.Room;
import abbtech.finalproject.HotelService.kafka.producer.HotelEventProducer;
import abbtech.finalproject.HotelService.repository.BookingRepository;
import abbtech.finalproject.HotelService.repository.HotelRepository;
import abbtech.finalproject.HotelService.repository.RoomRepository;
import abbtech.finalproject.HotelService.service.impl.HotelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HotelServiceTest {

	private HotelServiceImpl hotelService;
	private HotelRepository hotelRepository;
	private RoomRepository roomRepository;
	private BookingRepository bookingRepository;
	private HotelEventProducer hotelEventProducer;

	@BeforeEach
	public void setUp() {
		hotelRepository = mock(HotelRepository.class);
		roomRepository = mock(RoomRepository.class);
		bookingRepository = mock(BookingRepository.class);
		hotelEventProducer = mock(HotelEventProducer.class);

		hotelService = new HotelServiceImpl();
		hotelService.hotelRepository = hotelRepository;
		hotelService.roomRepository = roomRepository;
		hotelService.bookingRepository = bookingRepository;
		hotelService.hotelEventProducer = hotelEventProducer;
	}

	@Test
	public void testGetAllHotels_Success() {
		Hotel hotel = new Hotel();
		hotel.setId(1L);
		hotel.setName("Hotel Paradise");
		hotel.setLocation("Istanbul");
		hotel.setPricePerNight(150.0);
		hotel.setAllInclusive(true);

		when(hotelRepository.findAll()).thenReturn(List.of(hotel));

		List<HotelResponse> response = hotelService.getAllHotels();

		assertEquals(1, response.size());
		assertEquals("Hotel Paradise", response.get(0).name());
	}

	@Test
	public void testSearchHotels_Success() {
		Hotel hotel = new Hotel();
		hotel.setId(1L);
		hotel.setName("Hotel Paradise");
		hotel.setLocation("Istanbul");
		hotel.setPricePerNight(150.0);
		hotel.setAllInclusive(true);

		when(hotelRepository.findAll()).thenReturn(List.of(hotel));

		HotelSearchRequest request = new HotelSearchRequest("Istanbul", "2025-03-15", "2025-03-20");
		List<HotelResponse> response = hotelService.searchHotels(request);

		assertEquals(1, response.size());
		assertEquals("Hotel Paradise", response.get(0).name());
	}

	@Test
	public void testBookHotel_Success() {
		Hotel hotel = new Hotel();
		hotel.setId(1L);
		hotel.setName("Hotel Paradise");

		Room room = new Room();
		room.setId(1L);
		room.setAvailable(true);
		room.setHotel(hotel);

		Booking booking = new Booking();
		booking.setId(1L);
		booking.setRoomId(1L);
		booking.setUserEmail("test@example.com");
		booking.setConfirmationCode("ABC123");

		when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
		when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
		when(roomRepository.save(any(Room.class))).thenReturn(room);

		HotelBookingResponse response = hotelService.bookHotel(1L, "test@example.com", "2025-03-15", "2025-03-20");

		assertEquals(1L, response.bookingId());
		assertEquals("Hotel Paradise", response.hotelName());
		assertEquals("ABC123", response.confirmationCode());
		verify(hotelEventProducer, times(1)).sendHotelBookedEvent("test@example.com", "Hotel Paradise");
	}

}
