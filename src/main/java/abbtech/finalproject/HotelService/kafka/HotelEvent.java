package abbtech.finalproject.HotelService.kafka;

public record HotelEvent(String userEmail,
                         String hotelName,
                         String eventType) {
}
