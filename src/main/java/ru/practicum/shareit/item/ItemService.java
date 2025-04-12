package ru.practicum.shareit.item;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

interface ItemService {
    ItemDto addNewItem(long userId, Item item);

    ItemDto updateItem(Long userId, Long itemId, Item item);

    Optional<ItemOwnerDto> getItem(Long itemId, Long userId);

    List<ItemOwnerDto> getItems(long userId);

    List<ItemDto> getSearch(String text);

    CommentDto addNewItemComment(Long userId, Long itemId, @Valid CommentDto commentDto);
}