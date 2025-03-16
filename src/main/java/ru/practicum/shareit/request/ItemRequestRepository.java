package ru.practicum.shareit.request;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemRequestRepository {
    private final Map<Long, ItemRequest> itemRequests = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public ItemRequest save(ItemRequest itemRequest) {
        if (itemRequest.getId() == null) {
            itemRequest.setId(counter.incrementAndGet());
        }
        itemRequests.put(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    public Optional<ItemRequest> findById(Long id) {
        return Optional.ofNullable(itemRequests.get(id));
    }

    public Collection<ItemRequest> findAll() {
        return itemRequests.values();
    }

    public void deleteById(Long id) {
        itemRequests.remove(id);
    }
}

