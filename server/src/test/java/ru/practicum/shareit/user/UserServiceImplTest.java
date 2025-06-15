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

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
//        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final EntityManager em;
    private final UserService service;

//    @Test
//    void testSaveUser() {
//        UserDto userDto = makeUserDto("some@email.com", "Пётр", "Иванов");
//
//        service.saveUser(userDto);
//
//        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
//        User user = query.setParameter("email", userDto.getEmail())
//                .getSingleResult();
//
//        assertThat(user.getId(), notNullValue());
//        assertThat(user.getFirstName(), equalTo(userDto.getFirstName()));
//        assertThat(user.getLastName(), equalTo(userDto.getLastName()));
//        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
//        assertThat(user.getState(), equalTo(userDto.getState()));
//        assertThat(user.getRegistrationDate(), notNullValue());
//    }

    @Test
    void getAllUsers() {
        UserDto userDto1 = makeUserDto("Тимоха", "timoha@email.com");
        UserDto userDto2 = makeUserDto("Юляша", "youlyasha@email.com");

        service.createUser(userDto1);
        service.createUser(userDto2);

        TypedQuery<User> query = em.createQuery("Select u from User u", User.class);
        List<User> users = query.getResultList();
//        List<UserDto> userDtos = users.stream()
//                .map(UserMapper::toUserDto)
//                .toList();

//        assertThat(userDtos, hasItem(userDto1));
//        assertThat(userDtos, allOf(hasItem(userDto1), hasItem(userDto2)));
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