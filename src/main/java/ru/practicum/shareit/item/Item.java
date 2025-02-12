package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id; // уникальный идентификатор вещи
    @NotEmpty
    private String name; // краткое название
    @NotEmpty
    private String description; // развёрнутое описание
    @NotNull
    private Boolean available; // доступность для аренды
    private User owner; // владелец вещи
    private ItemRequest request; // запрос на создание вещи, если есть
}
