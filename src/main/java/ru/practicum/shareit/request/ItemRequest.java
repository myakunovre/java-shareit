package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    private Long id; // уникальный идентификатор запроса
    private String description; // текст запроса
    private User requestor; // пользователь, создавший запрос
    private LocalDateTime created; // дата и время создания запроса
}
