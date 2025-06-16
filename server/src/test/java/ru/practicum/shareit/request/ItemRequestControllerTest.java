package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void createRequest() throws Exception {
        Long userId = 1L;
        ItemRequestDto requestDto = new ItemRequestDto(
                null,
                "Test description",
                LocalDateTime.now(),
                Collections.emptyList()
        );
        ItemRequestDto expectedResponse = new ItemRequestDto(
                1L,
                "Test description",
                LocalDateTime.now(),
                Collections.emptyList()
        );

        when(requestService.createItemRequest(eq(userId), any(ItemRequestDto.class)))
                .thenReturn(expectedResponse);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.description").value(expectedResponse.getDescription()));

        verify(requestService, times(1)).createItemRequest(eq(userId), any(ItemRequestDto.class));
    }

    @Test
    void getUserItemRequests() throws Exception {
        Long userId = 1L;
        ItemRequestDto requestDto = new ItemRequestDto(
                1L,
                "Test description",
                LocalDateTime.now(),
                List.of(new ItemRequestResponse(1L, "Item1", 2L))
        );
        List<ItemRequestDto> expectedResponse = List.of(requestDto);

        when(requestService.getUserItemRequests(userId)).thenReturn(expectedResponse);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedResponse.get(0).getId()))
                .andExpect(jsonPath("$[0].description").value(expectedResponse.get(0).getDescription()))
                .andExpect(jsonPath("$[0].items[0].itemId").value(expectedResponse.get(0).getItems().get(0).getItemId()));

        verify(requestService, times(1)).getUserItemRequests(userId);
    }

    @Test
    void getAllItemRequests() throws Exception {
        Long userId = 1L;
        ItemRequestDto requestDto = new ItemRequestDto(
                1L,
                "Test description",
                LocalDateTime.now(),
                List.of(new ItemRequestResponse(1L, "Item1", 2L))
        );
        List<ItemRequestDto> expectedResponse = List.of(requestDto);

        when(requestService.getAllItemRequests(userId))
                .thenReturn(expectedResponse);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedResponse.get(0).getId()))
                .andExpect(jsonPath("$[0].description").value(expectedResponse.get(0).getDescription()))
                .andExpect(jsonPath("$[0].items[0].itemId").value(expectedResponse.get(0).getItems().get(0).getItemId()));

        verify(requestService, times(1)).getAllItemRequests(userId);
    }

    @Test
    void getRequest() throws Exception {
        Long requestId = 1L;
        ItemRequestDto expectedResponse = new ItemRequestDto(
                requestId,
                "Test description",
                LocalDateTime.now(),
                List.of(new ItemRequestResponse(1L, "Item1", 2L))
        );

        when(requestService.getItemRequest(requestId))
                .thenReturn(expectedResponse);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.description").value(expectedResponse.getDescription()))
                .andExpect(jsonPath("$.items[0].itemId").value(expectedResponse.getItems().get(0).getItemId()));

        verify(requestService, times(1)).getItemRequest(requestId);
    }
}
