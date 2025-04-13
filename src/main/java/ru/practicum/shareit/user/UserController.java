package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST /users - Adding user: {}", userDto);
        return userService.createUser(userDto);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("GET /users/{} - Getting user by id", id);
        return userService.getUser(id).orElse(null); // Возвращаем null, если не нашли пользователя
    }

    @PatchMapping("/{userId}")
    public User update(
            @PathVariable Long userId,
            @RequestBody UserDto userDto) {
        log.info("PATCH /users/{} - Updating user: {} by id {}", userId, userDto, userId);
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        log.info("DELETE /users/{} - Removing user", id);
        userService.deleteById(id);
    }
}
