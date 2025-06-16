package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void addBooking() throws Exception {
        long userId = 1L;
        BookingDtoInput bookingDtoInput = new BookingDtoInput(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                1L
        );
        BookingDtoOutput expectedBooking = new BookingDtoOutput(
                1L,
                bookingDtoInput.getStart(),
                bookingDtoInput.getEnd(),
                new ItemDto(1L, "Item", "Description", true, null),
                new UserDto(1L, "User", "user@example.com"),
                "WAITING"
        );

        when(bookingService.addNewBooking(userId, bookingDtoInput))
                .thenReturn(expectedBooking);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(bookingDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedBooking.getId()))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.item.id").value(expectedBooking.getItem().getId()))
                .andExpect(jsonPath("$.booker.id").value(expectedBooking.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(expectedBooking.getStatus()));

        verify(bookingService, times(1)).addNewBooking(eq(1L), any(BookingDtoInput.class));
    }

    @Test
    void updateBooking() throws Exception {
        long userId = 1L;
        long bookingId = 1L;
        Boolean approved = true;
        BookingDtoOutput expectedBooking = new BookingDtoOutput(
                bookingId,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                new ItemDto(1L, "Item", "Description", true, null),
                new UserDto(1L, "User", "user@example.com"),
                "APPROVED"
        );

        when(bookingService.updateBooking(userId, bookingId, approved))
                .thenReturn(expectedBooking);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedBooking.getId()))
                .andExpect(jsonPath("$.status").value(expectedBooking.getStatus()));
    }

    @Test
    void getBooking() throws Exception {
        long userId = 1L;
        long bookingId = 1L;
        BookingDtoOutput expectedBooking = new BookingDtoOutput(
                bookingId,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                new ItemDto(1L, "Item", "Description", true, null),
                new UserDto(1L, "User", "user@example.com"),
                "APPROVED"
        );

        when(bookingService.getBooking(userId, bookingId))
                .thenReturn(expectedBooking);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedBooking.getId()))
                .andExpect(jsonPath("$.status").value(expectedBooking.getStatus()));
    }

    @Test
    void getUserBookings() throws Exception {
        // Arrange
        long userId = 1L;
        BookingState state = BookingState.ALL;
        List<BookingDtoOutput> expectedBookings = List.of(
                new BookingDtoOutput(
                        1L,
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2),
                        new ItemDto(1L, "Item", "Description", true, null),
                        new UserDto(1L, "User", "user@example.com"),
                        "APPROVED"
                )
        );

        when(bookingService.getUserBookings(userId, state))
                .thenReturn(expectedBookings);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedBookings.get(0).getId()))
                .andExpect(jsonPath("$[0].status").value(expectedBookings.get(0).getStatus()));
    }

    @Test
    void getItemBookings() throws Exception {
        long userId = 1L;
        BookingState state = BookingState.ALL;
        List<BookingDtoOutput> expectedBookings = List.of(
                new BookingDtoOutput(
                        1L,
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2),
                        new ItemDto(1L, "Item", "Description", true, null),
                        new UserDto(1L, "User", "user@example.com"),
                        "APPROVED"
                )
        );

        when(bookingService.getItemBookings(userId, state))
                .thenReturn(expectedBookings);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedBookings.get(0).getId()))
                .andExpect(jsonPath("$[0].status").value(expectedBookings.get(0).getStatus()));
    }
}