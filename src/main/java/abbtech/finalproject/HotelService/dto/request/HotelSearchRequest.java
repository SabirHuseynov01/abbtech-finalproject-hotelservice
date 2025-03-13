package abbtech.finalproject.HotelService.dto.request;

public record HotelSearchRequest(String location,
                                 String checkInDate,
                                 String checkOutDate) {
}
