package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;

import java.util.List;

public interface BookingService {
    BookingDtoOutput addNewBooking(long userId, BookingDtoInput bookingDtoInput);

    BookingDtoOutput updateBooking(long userId, long bookingId, /*Booking booking, */Boolean approved);

    BookingDtoOutput getBooking(Long userId, long bookingId);

    List<BookingDtoOutput> getUserBookings(long userId, BookingState state);

    List<BookingDtoOutput> getItemBookings(long userId, BookingState state);
}

