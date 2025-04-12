package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PostMapping
    public BookingDtoOutput addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.addNewBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOutput updateBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long bookingId,
                                          @RequestParam(value = "approved") Boolean approved) {
        return bookingService.updateBooking(userId, bookingId, /*booking,*/ approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOutput getBooking(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long bookingId) {
        return bookingService.getBooking(userId, bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
    }

    @GetMapping
    public List<BookingDtoOutput> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "state", defaultValue = "ALL") BookingState state) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoOutput> getItemBookings(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "state", defaultValue = "ALL") BookingState state) {
        return bookingService.getItemBookings(userId, state);
    }
}
