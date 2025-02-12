package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name; // краткое название
    private String description; // развёрнутое описание
    private boolean available; // доступность для аренды
//    private Long requestId; // идентификатор запроса, если есть
}
