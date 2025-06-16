package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {

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

    public static Booking toBooking(BookingDtoInput bookingDtoInput, Item item, User booker, BookingStatus status) {
        return new Booking(null,
                bookingDtoInput.getStart(),
                bookingDtoInput.getEnd(),
                item,
                booker,
                status
        );
    }
}
