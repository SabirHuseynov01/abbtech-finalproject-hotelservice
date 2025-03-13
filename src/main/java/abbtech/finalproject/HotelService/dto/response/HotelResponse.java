package abbtech.finalproject.HotelService.dto.response;

public record HotelResponse(Long id,
                            String name,
                            String location,
                            double pricePerNight,
                            boolean allInclusive) {
}
