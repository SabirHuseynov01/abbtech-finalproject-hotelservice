package abbtech.finalproject.HotelService.dto.response;

public record RoomResponse(Long id,
                           String type,
                           double price,
                           boolean available) {
}
