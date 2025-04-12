package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemServiceImpl itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @Valid @RequestBody Item item) {
        return itemService.addNewItem(userId, item);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addItemComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @Valid @RequestBody CommentDto commentDto,
                                     @PathVariable long itemId) {
        return itemService.addNewItemComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody Item item,
                              @PathVariable long itemId) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemOwnerDto getItem(
            @PathVariable long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItem(itemId, userId).orElse(null);
    }

    @GetMapping
    public List<ItemOwnerDto> getAllItems(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearch(
            @RequestParam String text) {
        return itemService.getSearch(text);
    }
}
