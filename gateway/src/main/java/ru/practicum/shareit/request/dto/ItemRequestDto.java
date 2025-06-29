package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotEmpty
    private String description;
    private LocalDateTime created;
    private List<ItemRequestResponse> items;
}
