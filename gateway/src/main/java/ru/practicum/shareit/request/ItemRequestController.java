package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient requestClient;
    private final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createRequest(@RequestHeader(X_SHARER_USER_ID_HEADER) Long userId,
                                                @Valid @RequestBody ItemRequestDto request) {
        return requestClient.addNewItemRequest(userId, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserItemRequests(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId) {
        return requestClient.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader(X_SHARER_USER_ID_HEADER) long userId) {
        return requestClient.getAllItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable long requestId) {
        return requestClient.getItemRequest(requestId);
    }
}
