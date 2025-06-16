package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingDtoOutput addBooking(@RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
                                       @RequestBody BookingDtoInput bookingDtoInput) {
        return bookingService.addNewBooking(userId, bookingDtoInput);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOutput updateBooking(@RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
                                          @PathVariable long bookingId,
                                          @RequestParam Boolean approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOutput getBooking(@RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
                                       @PathVariable long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOutput> getUserBookings(@RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
                                                  @RequestParam(value = "state", defaultValue = "ALL") BookingState state) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoOutput> getItemBookings(@RequestHeader(X_SHARER_USER_ID_HEADER) long userId,
                                                  @RequestParam(value = "state", defaultValue = "ALL") BookingState state) {
        return bookingService.getItemBookings(userId, state);
    }
}
