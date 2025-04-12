package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus().toString());
    }

    public static BookingDtoOutput toBookingDtoOutput(Booking booking) {
        ItemDto itemDto = ItemMapper.toItemDto(booking.getItem());
        UserDto userDto = UserMapper.toUserDto(booking.getBooker());

        return new BookingDtoOutput(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemDto,
                userDto,
                booking.getStatus().toString());
    }

    public static Booking toBooking(BookingDto bookingDto, Item item, User booker, BookingStatus status) {
        return new Booking(null,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                booker,
                status
        );
    }
}
