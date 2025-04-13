package ru.practicum.shareit.booking;

import java.util.List;

interface BookingService {
    BookingDtoOutput addNewBooking(long userId, BookingDto bookingDto);

    BookingDtoOutput updateBooking(long userId, long bookingId, /*Booking booking, */Boolean approved);

    BookingDtoOutput getBooking(Long userId, long bookingId);

    List<BookingDtoOutput> getUserBookings(long userId, BookingState state);

    List<BookingDtoOutput> getItemBookings(long userId, BookingState state);
}

