package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoInput;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingClient bookingClient;
    private final String xSharerUserIdHeader = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(xSharerUserIdHeader) long userId,
                                             @Valid @RequestBody BookingDtoInput bookingDtoInput) {
        return bookingClient.addNewBooking(userId, bookingDtoInput);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(xSharerUserIdHeader) long userId,
                                                @PathVariable long bookingId,
                                                @RequestParam Boolean approved) {
        return bookingClient.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(
            @RequestHeader(xSharerUserIdHeader) long userId,
            @PathVariable long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(
            @RequestHeader(xSharerUserIdHeader) long userId,
            @RequestParam(value = "state", defaultValue = "ALL") BookingState state) {
        return bookingClient.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getItemBookings(
            @RequestHeader(xSharerUserIdHeader) long userId,
            @RequestParam(value = "state", defaultValue = "ALL") BookingState state) {
        return bookingClient.getItemBookings(userId, state);
    }
}
