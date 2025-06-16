package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestHeader(X_SHARER_USER_ID_HEADER) Long userId,
                           @RequestBody ItemDto itemDto) {
        return itemService.addNewItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addItemComment(@RequestHeader(X_SHARER_USER_ID_HEADER) Long userId,
                                     @RequestBody CommentInputDto commentInputDto,
                                     @PathVariable long itemId) {
        return itemService.addNewItemComment(userId, itemId, commentInputDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(X_SHARER_USER_ID_HEADER) Long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable long itemId) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemOwnerDto getItem(@PathVariable long itemId,
                                @RequestHeader(X_SHARER_USER_ID_HEADER) Long userId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemOwnerDto> getAllItems(@RequestHeader(X_SHARER_USER_ID_HEADER) Long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearch(@RequestParam String text) {
        return itemService.getSearch(text);
    }
}
