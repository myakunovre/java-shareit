package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDtoOutput addNewBooking(long userId, BookingDtoInput bookingDtoInput) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = itemRepository.findById(bookingDtoInput.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (!item.getAvailable()) {
            throw new BadRequestException("Item id = " + item.getId() + " is not available for booking");
        }

        if (bookingDtoInput.getStart() == null || bookingDtoInput.getEnd() == null) {
            throw new BadRequestException("Start and end dates must be specified");
        }

        if (bookingDtoInput.getEnd().isBefore(bookingDtoInput.getStart())) {
            throw new BadRequestException("End date cannot be before start date");
        }

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDtoOutput(savedBooking);
    }

    @Override
    public BookingDtoOutput updateBooking(long userId, long bookingId, Boolean approved) {

        userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (userId == booking.getItem().getOwner().getId()) {

            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }

            Booking savedBooking = bookingRepository.save(booking);
            return BookingMapper.toBookingDtoOutput(savedBooking);
        }
        throw new NotFoundException("User id = " + userId + " is not booker of booking id = " + bookingId);
    }

    public BookingDtoOutput getBooking(Long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (userId.equals(booking.getBooker().getId()) || userId.equals(booking.getItem().getOwner().getId())) {
            return BookingMapper.toBookingDtoOutput(booking);
        }
        throw new NotFoundException("User id = " + userId + " is not booker of booking id = " + bookingId
                + "or not owner of item id = " + booking.getItem().getId());
    }

    @Override
    public List<BookingDtoOutput> getUserBookings(long userId, BookingState state) {
        userNotFoundValidate(userId);

        LocalDateTime currentDateTime = LocalDateTime.now();

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.getAllUserBookings(userId);
            case CURRENT -> bookingRepository.getCurrentUserBookings(userId, currentDateTime);
            case PAST -> bookingRepository.getPastUserBookings(userId, currentDateTime);
            case FUTURE -> bookingRepository.getFutureUserBookings(userId, currentDateTime);
            case WAITING -> bookingRepository.getWaitingUserBookings(userId);
            case REJECTED -> bookingRepository.getRejectedUserBookings(userId);
            default -> throw new IllegalArgumentException("Unknown booking state: " + state);
        };

        return bookings.stream()
                .map(BookingMapper::toBookingDtoOutput)
                .toList();
    }


    @Override
    public List<BookingDtoOutput> getItemBookings(long userId, BookingState state) {
        userNotFoundValidate(userId);

        List<Item> items = itemRepository.findByOwnerId(userId);

        List<Booking> bookingsResult = new ArrayList<>();

        LocalDateTime currentDateTime = LocalDateTime.now();

        for (Item item : items) {
            long itemId = item.getId();

            List<Booking> bookings = switch (state) {
                case ALL -> bookingRepository.getAllItemBookings(itemId);
                case CURRENT -> bookingRepository.getCurrentItemBookings(itemId, currentDateTime);
                case PAST -> bookingRepository.getPastItemBookings(itemId, currentDateTime);
                case FUTURE -> bookingRepository.getFutureItemBookings(itemId, currentDateTime);
                case WAITING -> bookingRepository.getWaitingItemBookings(itemId);
                case REJECTED -> bookingRepository.getRejectedItemBookings(itemId);
                default -> throw new IllegalArgumentException("Unknown booking state: " + state);
            };

            bookingsResult.addAll(bookings);
        }

        return bookingsResult.stream()
                .map(BookingMapper::toBookingDtoOutput)
                .toList();
    }

    private void userNotFoundValidate(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}

