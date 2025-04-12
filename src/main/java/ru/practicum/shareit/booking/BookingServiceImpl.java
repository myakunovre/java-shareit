package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDtoOutput addNewBooking(long userId, BookingDto bookingDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (bookingDto.getItemId() == null) {
            throw new BadRequestException("Booking object must contain Item object");
        }

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (!item.getAvailable()) {
            throw new BadRequestException("Item id = " + item.getId() + " is not available for booking");
        }

        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new BadRequestException("Start and end dates must be specified");
        }

        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BadRequestException("End date cannot be before start date");
        }

        Booking booking = BookingMapper.toBooking(bookingDto, item, user, BookingStatus.WAITING);

        Booking booking1 = bookingRepository.save(booking);
        return BookingMapper.toBookingDtoOutput(booking1);
    }

    @Override
    public BookingDtoOutput updateBooking(long userId, long bookingId, /*Booking booking, */Boolean approved) {

        userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (userId == booking.getItem().getOwner().getId()) {

            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }

            Booking booking1 = bookingRepository.save(booking);
            return BookingMapper.toBookingDtoOutput(booking1);
        }
        throw new NotFoundException("User id = " + userId + " is not booker of booking id = " + bookingId);
    }

    public Optional<BookingDtoOutput> getBooking(Long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (userId.equals(booking.getBooker().getId()) || userId.equals(booking.getItem().getOwner().getId())) {
            Booking booking1 = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new NotFoundException("Booking not found"));

            BookingDtoOutput bookingDtoOutput = BookingMapper.toBookingDtoOutput(booking1);

            return Optional.of(bookingDtoOutput);
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

        List<Booking> bookings = new ArrayList<>();

        LocalDateTime currentDateTime = LocalDateTime.now();

        for (Item item : items) {
            long itemId = item.getId();

            List<Booking> bookings1 = switch (state) {
                case ALL -> bookingRepository.getAllItemBookings(itemId);
                case CURRENT -> bookingRepository.getCurrentItemBookings(itemId, currentDateTime);
                case PAST -> bookingRepository.getPastItemBookings(itemId, currentDateTime);
                case FUTURE -> bookingRepository.getFutureItemBookings(itemId, currentDateTime);
                case WAITING -> bookingRepository.getWaitingItemBookings(itemId);
                case REJECTED -> bookingRepository.getRejectedItemBookings(itemId);
                default -> throw new IllegalArgumentException("Unknown booking state: " + state);
            };

            bookings.addAll(bookings1);
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDtoOutput)
                .toList();
    }

    private void userNotFoundValidate(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}

