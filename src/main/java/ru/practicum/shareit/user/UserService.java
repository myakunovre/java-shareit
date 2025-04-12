package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

interface UserService {
    User createUser(UserDto userDto);

    Optional<User> getUser(Long id);

    Collection<UserDto> getAllUsers();

    User updateUser(UserDto userDto, long userId);

    void deleteById(Long id);
}