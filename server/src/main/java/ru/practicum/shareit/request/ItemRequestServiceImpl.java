package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestDto createItemRequest(Long userId, ItemRequestDto request) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        request.setCreated(LocalDateTime.now());
        ItemRequest requestToSave = ItemRequestMapper.toItemRequest(request, requestor);
        ItemRequest savedItemRequest = itemRequestRepository.save(requestToSave);
        return ItemRequestMapper.toItemRequestDto(savedItemRequest);
    }

    public List<ItemRequestDto> getUserItemRequests(long userId) {
        userNotFoundValidate(userId);

        List<ItemRequest> itemRequests = itemRequestRepository.getUserItemRequests(userId);

        List<ItemRequestDto> itemRequestDtos = itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();

        for (ItemRequestDto itemRequestDto : itemRequestDtos) {
            itemRequestDto.setItems(getResponses(itemRequestDto));
        }
        return itemRequestDtos;
    }


    public List<ItemRequestDto> getAllItemRequests(Long userId) {
        userNotFoundValidate(userId);

        List<ItemRequest> itemRequests = itemRequestRepository.getAllItemRequests(userId);

        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    public ItemRequestDto getItemRequest(Long id) {
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ItemRequest not found"));

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        List<ItemRequestResponse> items = getResponses(itemRequestDto);
        itemRequestDto.setItems(items);

        return itemRequestDto;
    }

    private void userNotFoundValidate(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private List<ItemRequestResponse> getResponses(ItemRequestDto itemRequestDto) {
        List<Item> items = itemRepository.findByRequestId(itemRequestDto.getId());

        List<ItemRequestResponse> itemRequestResponses = new ArrayList<>();
        for (Item item : items) {
            itemRequestResponses.add(new ItemRequestResponse(
                    item.getId(),
                    item.getName(),
                    item.getOwner().getId()));
        }
        return itemRequestResponses;
    }
}

