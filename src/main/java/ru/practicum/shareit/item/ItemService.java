package ru.practicum.shareit.item;

import jakarta.validation.Valid;

import java.util.List;

interface ItemService {
    ItemDto addNewItem(long userId, Item item);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemOwnerDto getItem(Long itemId, Long userId);

    List<ItemOwnerDto> getItems(long userId);

    List<ItemDto> getSearch(String text);

    CommentDto addNewItemComment(Long userId, Long itemId, @Valid CommentDto commentDto);
}