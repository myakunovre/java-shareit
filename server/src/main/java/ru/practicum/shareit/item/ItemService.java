package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(long userId, ItemDto item);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemOwnerDto getItem(Long itemId, Long userId);

    List<ItemOwnerDto> getAllItems(long userId);

    List<ItemDto> getSearch(String text);

    CommentDto addNewItemComment(Long userId, Long itemId, CommentInputDto commentInputDto);
}

