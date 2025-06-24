package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.dto.ItemDto;
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
class ItemServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private EntityManager em;

    UserDto user;

    @BeforeEach
    void setUp() {
        UserDto userDto = makeUserDto("Тимофей Изюмов", "t_izyumov@email.com");
        user = userService.createUser(userDto);
    }

    @Test
    void testGetUserItems() {

        ItemDto itemDto1 = makeItemDto("Кость", "Кость немного покусанная");
        itemService.addNewItem(user.getId(), itemDto1);

        ItemDto itemDto2 = makeItemDto("Бигуди", "Бигуди для афрокудрей");
        itemService.addNewItem(user.getId(), itemDto2);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.owner.id = :ownerId", Item.class);
        List<Item> items = query.setParameter("ownerId", user.getId()).getResultList();

        assertThat(items.size(), equalTo(2));

        Item foundedItem1 = items.get(0);
        assertThat(foundedItem1.getId(), notNullValue());
        assertThat(foundedItem1.getDescription(), equalTo(itemDto1.getDescription()));

        Item foundedItem2 = items.get(1);
        assertThat(foundedItem2.getId(), notNullValue());
        assertThat(foundedItem2.getDescription(), equalTo(itemDto2.getDescription()));
    }

    private UserDto makeUserDto(String name, String email) {
        return new UserDto(
                null,
                name,
                email);
    }

    private ItemDto makeItemDto(String name, String description) {
        return new ItemDto(
                null,
                name,
                description,
                true,
                null);
    }
}