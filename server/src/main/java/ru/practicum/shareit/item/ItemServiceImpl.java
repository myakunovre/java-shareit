package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = ItemMapper.toItem(itemDto, owner);

        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Request not found"));
            item.setRequest(itemRequest);
        }

        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (userId.equals(item.getOwner().getId())) {

            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }

            Item savedItem = itemRepository.save(item);
            return ItemMapper.toItemDto(savedItem);
        }
        throw new NotFoundException("User id = " + userId + " is not owner of item id = " + itemId);
    }

    public ItemOwnerDto getItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        LocalDateTime lastDate = null;
        LocalDateTime nextDate = null;

        if (userId.equals(item.getOwner().getId())) {

            List<Booking> bookings = bookingRepository.getAllItemBookings(item.getId());

            List<LocalDateTime> pastDates = new ArrayList<LocalDateTime>();
            List<LocalDateTime> nextDates = new ArrayList<LocalDateTime>();

            for (Booking booking : bookings) {
                if (booking.getEnd().isBefore(LocalDateTime.now())) {
                    pastDates.add(booking.getEnd());
                }
                if (booking.getStart().isAfter(LocalDateTime.now())) {
                    nextDates.add(booking.getStart());
                }
            }

            if (!pastDates.isEmpty()) {
                Arrays.sort(new List[]{pastDates}, Collections.reverseOrder());
                lastDate = pastDates.getFirst();
            }

            if (!nextDates.isEmpty()) {
                Arrays.sort(new List[]{nextDates});
                nextDate = nextDates.getFirst();
            }
        }

        List<Comment> comments = commentRepository.findAllByItemId(item.getId());
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentMapper::toCommentDto)
                .toList();

        return ItemMapper.toItemOwnerDto(item, lastDate, nextDate, commentDtos);
    }

    @Override
    public List<ItemOwnerDto> getAllItems(long userId) {
        List<Item> items = itemRepository.findByOwnerId(userId);

        List<ItemOwnerDto> itemOwnerDtos = new ArrayList<>();

        for (Item item : items) {
            List<Booking> bookings = bookingRepository.getAllItemBookings(item.getId());

            List<LocalDateTime> pastDates = new ArrayList<LocalDateTime>();
            List<LocalDateTime> nextDates = new ArrayList<LocalDateTime>();

            for (Booking booking : bookings) {
                if (booking.getEnd().isBefore(LocalDateTime.now())) {
                    pastDates.add(booking.getEnd());
                }
                if (booking.getStart().isAfter(LocalDateTime.now())) {
                    nextDates.add(booking.getStart());
                }
            }

            LocalDateTime lastBookingDate = null;
            LocalDateTime nextBookingDate = null;

            if (!pastDates.isEmpty()) {
                Arrays.sort(new List[]{pastDates}, Collections.reverseOrder());
                lastBookingDate = pastDates.getFirst();
            }

            if (!nextDates.isEmpty()) {
                Arrays.sort(new List[]{nextDates});
                nextBookingDate = nextDates.getFirst();
            }

            List<Comment> comments = commentRepository.findAllByItemId(item.getId());

            List<CommentDto> commentDtos = comments.stream()
                    .map(CommentMapper::toCommentDto)
                    .toList();

            itemOwnerDtos.add(ItemMapper.toItemOwnerDto(item, lastBookingDate, nextBookingDate, commentDtos));
        }
        return itemOwnerDtos;
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        List<Item> items = itemRepository.getSearch(text);

        return items.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public CommentDto addNewItemComment(Long userId, Long itemId, CommentInputDto commentInputDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        List<Booking> bookings = bookingRepository.getAllItemBookings(itemId);

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        List<Long> bookerIds = bookings.stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.APPROVED))
                .filter(booking -> booking.getEnd().truncatedTo(ChronoUnit.SECONDS).isBefore(now))
                .map(booking -> booking.getBooker().getId())
                .toList();

        if (bookerIds.contains(userId)) {
            Comment comment = CommentMapper.toComment(commentInputDto, item, user, now);

            Comment savedComment = commentRepository.save(comment);

            return CommentMapper.toCommentDto(savedComment);
        }

        throw new BadRequestException("User id = " + userId + "was not the booker of the item id = " + itemId);
    }
}