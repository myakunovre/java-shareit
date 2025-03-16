package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto addNewItem(long userId, Item item) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        item.setOwner(user);
        Item item1 = itemRepository.save(item);
        return ItemMapper.toItemDto(item1);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, Item item) {
        Item item1 = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (userId.equals(item1.getOwner().getId())) {

            if (item.getName() != null) {
                item1.setName(item.getName());
            }
            if (item.getDescription() != null) {
                item1.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                item1.setAvailable(item.getAvailable());
            }

            Item item2 = itemRepository.save(item1);
            return ItemMapper.toItemDto(item2);
        }
        throw new NotFoundException("User id = " + userId + " is not owner of item id = " + itemId);
    }

    public Optional<ItemDto> getItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        ItemDto itemDto = ItemMapper.toItemDto(item);
        return Optional.of(itemDto);
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        List<Item> items = itemRepository.findByUserId(userId);

        return items.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        List<Item> items = itemRepository.getSearch(text);

        return items.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}