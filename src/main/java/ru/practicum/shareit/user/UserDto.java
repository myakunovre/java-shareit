package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDto {
    private String name; // имя или логин пользователя
    @NotNull
    @Email(message = "Некорректный формат email")
    private String email; // адрес электронной почты
}
