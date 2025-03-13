package abbtech.finalproject.HotelService.service;

import abbtech.finalproject.HotelService.dto.request.HotelSearchRequest;
import abbtech.finalproject.HotelService.dto.response.HotelBookingResponse;
import abbtech.finalproject.HotelService.dto.response.HotelResponse;

import java.util.List;

public interface HotelService {
    List<HotelResponse> getAllHotels();

    List<HotelResponse> searchHotels(HotelSearchRequest request);
    HotelBookingResponse bookHotel(Long roomId,
                                   String userEmail,
                                   String checkInDate,
                                   String checkOutDate);
}
