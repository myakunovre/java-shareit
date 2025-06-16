package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;
    private final String xSharerUserIdHeader = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(xSharerUserIdHeader) Long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        return itemClient.addNewItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItemComment(@RequestHeader(xSharerUserIdHeader) Long userId,
                                                 @Valid @RequestBody CommentInputDto commentInputDto,
                                                 @PathVariable Long itemId) {
        return itemClient.addNewItemComment(userId, itemId, commentInputDto);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(xSharerUserIdHeader) Long userId,
                                             @Valid @RequestBody ItemDto itemDto,
                                             @PathVariable Long itemId) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId,
                                          @RequestHeader(xSharerUserIdHeader) Long userId) {
        return itemClient.getItem(itemId, userId);
    }


    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(xSharerUserIdHeader) Long userId) {
        return itemClient.getAllItems(userId);
    }


    @GetMapping("/search")
    public ResponseEntity<Object> getSearch(@RequestParam String text) {
        return itemClient.getSearch(text);
    }
}

