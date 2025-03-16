package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequest createRequest(@RequestBody ItemRequest request) {
        return requestService.createItemRequest(request);
    }

    @GetMapping("/{id}")
    public ItemRequest getRequest(@PathVariable Long id) {
        return requestService.getItemRequest(id).orElse(null); // Возвращаем null, если не нашли пользователя
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequest> findAll() {
        return requestService.getAllItemRequests();
    }

    @PutMapping
    public ItemRequest update(@RequestBody ItemRequest updateRequest) {
        return requestService.updateItemRequest(updateRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteRequestById(@PathVariable long id) {
        requestService.deleteById(id);
    }
}
