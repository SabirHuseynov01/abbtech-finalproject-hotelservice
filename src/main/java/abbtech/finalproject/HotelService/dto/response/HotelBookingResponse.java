package abbtech.finalproject.HotelService.dto.response;

public record HotelBookingResponse(Long bookingId,
                                   String hotelName,
                                   String confirmationCode) {
}
