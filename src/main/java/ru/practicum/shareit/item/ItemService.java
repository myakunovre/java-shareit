package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

interface ItemService {
    ItemDto addNewItem(long userId, Item item);
    ItemDto updateItem(Long userId, Long itemId, Item item);
    Optional<ItemDto> getItem(Long itemId);
    List<ItemDto> getItems(long userId);
    List<ItemDto> getSearch(String text);
}