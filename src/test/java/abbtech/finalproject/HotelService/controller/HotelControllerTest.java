package abbtech.finalproject.HotelService.controller;

import abbtech.finalproject.HotelService.controller.HotelController;
import abbtech.finalproject.HotelService.dto.request.HotelSearchRequest;
import abbtech.finalproject.HotelService.dto.response.HotelBookingResponse;
import abbtech.finalproject.HotelService.dto.response.HotelResponse;
import abbtech.finalproject.HotelService.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
public class HotelControllerTest {


    private MockMvc mockMvc;

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelController hotelController;

    private ObjectMapper objectMapper;

    private HotelSearchRequest searchRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(hotelController).build();
        objectMapper = new ObjectMapper();
        searchRequest = new HotelSearchRequest("Istanbul", "2025-03-15", "2025-03-20");
    }

    @Test
    public void testGetAllHotels_Success() throws Exception {
        List<HotelResponse> hotels = List.of(
                new HotelResponse(1L, "Hotel Paradise", "Istanbul", 150.0, true)
        );
        when(hotelService.getAllHotels()).thenReturn(hotels);

        mockMvc.perform(get("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Hotel Paradise"));

        verify(hotelService, times(1)).getAllHotels();
    }

    @Test
    public void testSearchHotels_Success() throws Exception {
        List<HotelResponse> hotels = List.of(
                new HotelResponse(1L, "Hotel Paradise", "Istanbul", 150.0, true)
        );
        when(hotelService.searchHotels(any(HotelSearchRequest.class))).thenReturn(hotels);

        mockMvc.perform(get("/api/hotels/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Hotel Paradise"));

        verify(hotelService, times(1)).searchHotels(any(HotelSearchRequest.class));
    }

    @Test
    public void testBookHotel_Success() throws Exception {
        HotelBookingResponse response = new HotelBookingResponse(1L, "Hotel Paradise", "ABC123");
        when(hotelService.bookHotel(1L, "test@example.com", "2025-03-15", "2025-03-20")).thenReturn(response);

        mockMvc.perform(post("/api/hotels/book")
                        .param("roomId", "1")
                        .param("userEmail", "test@example.com")
                        .param("checkInDate", "2025-03-15")
                        .param("checkOutDate", "2025-03-20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1L))
                .andExpect(jsonPath("$.hotelName").value("Hotel Paradise"))
                .andExpect(jsonPath("$.confirmationCode").value("ABC123"));

        verify(hotelService, times(1)).bookHotel(1L, "test@example.com", "2025-03-15", "2025-03-20");
    }
}