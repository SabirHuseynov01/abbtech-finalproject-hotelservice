package abbtech.finalproject.HotelService.kafka.producer;

import abbtech.finalproject.HotelService.kafka.HotelEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class HotelEventProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendHotelBookedEvent(String userEmail, String hotelName) {
        try {
            HotelEvent event = new HotelEvent(userEmail, hotelName, "HOTEL_BOOKED");
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("hotel-events", eventJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send hotel event: " + e.getMessage());
        }
    }
}
