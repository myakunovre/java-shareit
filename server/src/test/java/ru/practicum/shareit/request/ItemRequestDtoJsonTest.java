package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testItemRequestDto() throws IOException {
        LocalDateTime created = LocalDateTime.now();

        ItemRequestResponse itemRequestResponse = new ItemRequestResponse(1L, "Item Name", 2L);
        List<ItemRequestResponse> items = Collections.singletonList(itemRequestResponse);

        ItemRequestDto itemRequestDto = new ItemRequestDto(
                1L,
                "Need a new item",
                created,
                items
        );

        JsonContent<ItemRequestDto> jsonContent = json.write(itemRequestDto);

        assertThat(jsonContent).hasJsonPath("$.id");
        assertThat(jsonContent).hasJsonPath("$.description");
        assertThat(jsonContent).hasJsonPath("$.created");
        assertThat(jsonContent).hasJsonPath("$.items");

        assertThat(jsonContent).extractingJsonPathStringValue("$.created")
                .isEqualTo(created.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        assertThat(jsonContent.getJson()).isEqualTo(
                "{" +
                        "\"id\":1," +
                        "\"description\":\"Need a new item\"," +
                        "\"created\":\"" + created.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\"," +
                        "\"items\":" +
                        "[{" +
                        "\"itemId\":1," +
                        "\"name\":\"Item Name\"," +
                        "\"ownerId\":2" +
                        "}]" +
                        "}"
        );
    }
}
