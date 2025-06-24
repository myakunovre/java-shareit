package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItServer;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final EntityManager em;
    private final UserService service;

    @Test
    void getAllUsers() {
        UserDto userDto1 = makeUserDto("Тимоха", "timoha@email.com");
        UserDto userDto2 = makeUserDto("Юляша", "youlyasha@email.com");

        service.createUser(userDto1);
        service.createUser(userDto2);

        TypedQuery<User> query = em.createQuery("Select u from User u", User.class);
        List<User> users = query.getResultList();

        assertThat(users.size(), equalTo(2));

        assertThat(users, containsInAnyOrder(
                Matchers.hasProperty("email", equalTo(userDto1.getEmail())),
                Matchers.hasProperty("email", equalTo(userDto2.getEmail()))
        ));
    }

    private UserDto makeUserDto(String name, String email) {
        return new UserDto(
                null,
                name,
                email);
    }
}