package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUserDto() throws IOException {
        UserDto userDto = new UserDto(
                1L,
                "John Doe",
                "john.doe@example.com"
        );

        JsonContent<UserDto> jsonContent = json.write(userDto);

        assertThat(jsonContent).hasJsonPath("$.id");
        assertThat(jsonContent).hasJsonPath("$.name");
        assertThat(jsonContent).hasJsonPath("$.email");

        String expectedJson = String.format(
                "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}"
        );

        assertThat(jsonContent.getJson()).isEqualTo(expectedJson);
    }
}
