package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static ItemOwnerDto toItemOwnerDto(Item item, LocalDateTime lastBookingDate, LocalDateTime nextBookingDate,
                                              List<CommentDto> commentDtos) {
        ItemOwnerDto itemOwnerDto = new ItemOwnerDto();
        itemOwnerDto.setId(item.getId());
        itemOwnerDto.setName(item.getName());
        itemOwnerDto.setDescription(item.getDescription());
        itemOwnerDto.setAvailable(item.getAvailable());

        if (lastBookingDate != null) {
            itemOwnerDto.setLastBooking(lastBookingDate);
        }

        if (nextBookingDate != null) {
            itemOwnerDto.setNextBooking(nextBookingDate);
        }

        itemOwnerDto.setComments(commentDtos);

        return itemOwnerDto;
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
        if (itemDto.getRequestId() != null) {
            item.setRequest(new ItemRequest());
        }
        return item;
    }
}