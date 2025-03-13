package abbtech.finalproject.HotelService.controller;

import abbtech.finalproject.HotelService.dto.request.HotelSearchRequest;
import abbtech.finalproject.HotelService.dto.response.HotelBookingResponse;
import abbtech.finalproject.HotelService.dto.response.HotelResponse;
import abbtech.finalproject.HotelService.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/search")
    public ResponseEntity<List<HotelResponse>> searchHotels(@RequestBody HotelSearchRequest request) {
        return ResponseEntity.ok(hotelService.searchHotels(request));
    }

    @PostMapping("/book")
    public ResponseEntity<HotelBookingResponse> bookHotel(
            @RequestParam Long roomId,
            @RequestParam String userEmail,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate) {
        return ResponseEntity.ok(hotelService.bookHotel(roomId, userEmail, checkInDate, checkOutDate));
    }
}