package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemRequestService requestService;

    @Autowired
    private EntityManager em;

    @Test
    void testGetItemRequest() {
        UserDto requestorDto = makeUserDto("Роман", "r_myakunov@email.com");
        UserDto savedRequestorDto = userService.createUser(requestorDto);

        ItemRequestDto requestDto = makeItemRequestDto("Электрофазотрон");
        ItemRequestDto savedRequestDto = requestService.createItemRequest(savedRequestorDto.getId(), requestDto);

        TypedQuery<ItemRequest> query = em.createQuery("Select ir from ItemRequest ir where ir.id = :id", ItemRequest.class);
        ItemRequest request = query.setParameter("id", savedRequestDto.getId()).getSingleResult();

        assertThat(request.getId(), notNullValue());
        assertThat(request.getId(), equalTo(savedRequestDto.getId()));
        assertThat(request.getDescription(), equalTo(savedRequestDto.getDescription()));

        UserDto ownerDto1 = makeUserDto("Тимофей Изюмов", "t_izyumov@email.com");
        UserDto savedOwner1 = userService.createUser(ownerDto1);

        UserDto ownerDto2 = makeUserDto("Юлия Изюмова", "y_izyumova@email.com");
        UserDto savedOwner2 = userService.createUser(ownerDto2);

        ItemDto itemDto1 = makeItemDto("Электрофазотрон", "Хрен знает, что это такое", savedRequestDto.getId());
        ItemDto savedItemDto1 = itemService.addNewItem(savedOwner1.getId(), itemDto1);

        ItemDto itemDto2 = makeItemDto("Электрофазотрон", "Отличная вещь", savedRequestDto.getId());
        ItemDto savedItemDto2 = itemService.addNewItem(savedOwner2.getId(), itemDto2);

        ItemRequestDto itemRequestDto = requestService.getItemRequest(savedRequestDto.getId());
        List<ItemRequestResponse> items = itemRequestDto.getItems();

        assertThat(items.size(), equalTo(2));

        ItemRequestResponse response1 = items.get(0);
        assertThat(response1.getItemId(), equalTo(savedItemDto1.getId()));
        assertThat(response1.getName(), equalTo(savedItemDto1.getName()));
        assertThat(response1.getOwnerId(), equalTo(savedOwner1.getId()));

        ItemRequestResponse response2 = items.get(1);
        assertThat(response2.getItemId(), equalTo(savedItemDto2.getId()));
        assertThat(response2.getName(), equalTo(savedItemDto2.getName()));
        assertThat(response2.getOwnerId(), equalTo(savedOwner2.getId()));
    }

    private static ItemRequestDto makeItemRequestDto(String description) {
        return new ItemRequestDto(
                null,
                description,
                null,
                null);
    }

    private UserDto makeUserDto(String name, String email) {
        return new UserDto(
                null,
                name,
                email);
    }

    private ItemDto makeItemDto(String name, String description, Long requestId) {
        return new ItemDto(
                null,
                name,
                description,
                true,
                requestId);
    }
}