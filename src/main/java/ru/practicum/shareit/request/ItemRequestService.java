package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        return itemRequestRepository.save(itemRequest);
    }

    public Optional<ItemRequest> getItemRequest(Long id) {
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ItemRequest not found"));
        return Optional.of(itemRequest);
    }

    public Collection<ItemRequest> getAllItemRequests() {
        return itemRequestRepository.findAll();
    }

    public ItemRequest updateItemRequest(ItemRequest itemRequest) {
        ItemRequest itemRequest1 = itemRequestRepository.findById(itemRequest.getId())
                .orElseThrow(() -> new RuntimeException("ItemRequest not found"));
        itemRequest1.setDescription(itemRequest.getDescription());
        itemRequest1.setRequestor(itemRequest.getRequestor());

        return itemRequestRepository.save(itemRequest1);
    }

    public void deleteById(Long id) {
        itemRequestRepository.deleteById(id);
    }
}

