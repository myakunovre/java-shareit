package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private LocalDateTime start;

    private LocalDateTime end;

    @NotNull
    private Long itemId;

    private Long bookerId;

    private String status;
}