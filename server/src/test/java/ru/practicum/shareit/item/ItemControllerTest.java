package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemDto itemDto1;
    private ItemDto itemDto2;
    private ItemOwnerDto itemOwnerDto1;
    private ItemOwnerDto itemOwnerDto2;
    private List<ItemOwnerDto> itemOwnerDtos;
    private List<ItemDto> itemDtos;
    private CommentDto commentDto;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();


        itemDto1 = new ItemDto(
                1L,
                "Вещь1",
                "Очень нужная вещь1",
                true,
                11L);
        itemDto2 = new ItemDto(
                2L,
                "Вещь2",
                "Очень нужная вещь2",
                true,
                12L);

        itemDtos = new ArrayList<>();
        itemDtos.add(itemDto1);
        itemDtos.add(itemDto2);


        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("This is a great item!");
        commentDto.setAuthorName("John");
        commentDto.setCreated(LocalDateTime.now().minusDays(2));


        itemOwnerDto1 = new ItemOwnerDto(
                1L,
                "Вещь1",
                "Очень нужная вещь1",
                true,
                11L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                new ArrayList<>());

        itemOwnerDto2 = new ItemOwnerDto(
                2L,
                "Вещь2",
                "Очень нужная вещь2",
                true,
                12L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                new ArrayList<>());

        itemOwnerDtos = new ArrayList<>();
        itemOwnerDtos.add(itemOwnerDto1);
        itemOwnerDtos.add(itemOwnerDto2);
    }

    @Test
    void addItem() throws Exception {
        when(itemService.addNewItem(anyLong(), any(ItemDto.class)))
                .thenReturn(itemDto1);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 123L)
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("Вещь1")))
                .andExpect(jsonPath("$.description", is("Очень нужная вещь1")))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.requestId", is(11L), Long.class));

        verify(itemService, times(1)).addNewItem(eq(123L), any(ItemDto.class));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class)))
                .thenReturn(itemDto1);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 123L)
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("Вещь1")))
                .andExpect(jsonPath("$.description", is("Очень нужная вещь1")))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.requestId", is(11L), Long.class));

        verify(itemService, times(1)).updateItem(eq(123L), eq(1L), any(ItemDto.class));
    }

    @Test
    void getItem() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemOwnerDto1);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 123L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("Вещь1")))
                .andExpect(jsonPath("$.description", is("Очень нужная вещь1")))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.requestId", is(11L), Long.class))
                .andExpect(jsonPath("$.lastBooking").exists())
                .andExpect(jsonPath("$.nextBooking").exists());

        verify(itemService, times(1)).getItem(eq(1L), eq(123L));
    }

    @Test
    void getAllItems() throws Exception {
        when(itemService.getAllItems(anyLong()))
                .thenReturn(itemOwnerDtos);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 123L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].name", is("Вещь1")))
                .andExpect(jsonPath("$[0].description", is("Очень нужная вещь1")))
                .andExpect(jsonPath("$[0].available", is(true)))
                .andExpect(jsonPath("$[0].requestId", is(11L), Long.class))
                .andExpect(jsonPath("$[0].lastBooking").exists())
                .andExpect(jsonPath("$[0].nextBooking").exists())
                .andExpect(jsonPath("$[1].id", is(2L), Long.class))
                .andExpect(jsonPath("$[1].name", is("Вещь2")))
                .andExpect(jsonPath("$[1].description", is("Очень нужная вещь2")))
                .andExpect(jsonPath("$[1].available", is(true)))
                .andExpect(jsonPath("$[1].requestId", is(12L), Long.class))
                .andExpect(jsonPath("$[1].lastBooking").exists())
                .andExpect(jsonPath("$[1].nextBooking").exists());

        verify(itemService, times(1)).getAllItems(eq(123L));
    }

    @Test
    void getSearch() throws Exception {
        when(itemService.getSearch(eq("test")))
                .thenReturn(itemDtos);

        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("text", "test"))
                .andExpect(status().isOk()) // Ожидаем статус 200
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Вещь1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Вещь2"));

        verify(itemService, times(1)).getSearch(eq("test"));
    }

    @Test
    void addItemComment() throws Exception {
        CommentInputDto commentInputDto = new CommentInputDto();
        commentInputDto.setText("This is a great item!");

        when(itemService.addNewItemComment(eq(123L), eq(1L), any(CommentInputDto.class)))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 123L)
                        .content(mapper.writeValueAsString(commentInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("This is a great item!"));

        verify(itemService, times(1)).addNewItemComment(eq(123L), eq(1L), any(CommentInputDto.class));
    }
}