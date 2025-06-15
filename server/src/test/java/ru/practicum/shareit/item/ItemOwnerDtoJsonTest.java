package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemOwnerDtoJsonTest {

    @Autowired
    private JacksonTester<ItemOwnerDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testItemOwnerDto() throws IOException {
        LocalDateTime lastBooking = LocalDateTime.now();
        LocalDateTime nextBooking = LocalDateTime.now().plusDays(1);
        LocalDateTime created = LocalDateTime.now();

        CommentDto commentDto = new CommentDto(1L, "Great item!", "John", created);
        List<CommentDto> comments = Collections.singletonList(commentDto);

        ItemOwnerDto itemOwnerDto = new ItemOwnerDto(
                1L,
                "Item Name",
                "Item Description",
                true,
                123L,
                lastBooking,
                nextBooking,
                comments
        );

        var jsonContent = json.write(itemOwnerDto);

        assertThat(jsonContent).hasJsonPath("$.id");
        assertThat(jsonContent).hasJsonPath("$.name");
        assertThat(jsonContent).hasJsonPath("$.description");
        assertThat(jsonContent).hasJsonPath("$.available");
        assertThat(jsonContent).hasJsonPath("$.requestId");
        assertThat(jsonContent).hasJsonPath("$.lastBooking");
        assertThat(jsonContent).hasJsonPath("$.nextBooking");
        assertThat(jsonContent).hasJsonPath("$.comments");

        assertThat(jsonContent).extractingJsonPathStringValue("$.lastBooking")
                .isEqualTo(lastBooking.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(jsonContent).extractingJsonPathStringValue("$.nextBooking")
                .isEqualTo(nextBooking.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
