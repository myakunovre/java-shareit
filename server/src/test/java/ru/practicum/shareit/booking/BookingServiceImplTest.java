package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EntityManager em;

    @Test
    void testGetUserBookings() {

        UserDto ownerDto = makeUserDto("Тимофей Изюмов", "t_izyumov@email.com");
        UserDto savedOwner = userService.createUser(ownerDto);

        ItemDto itemDto1 = makeItemDto("Кость", "Кость немного покусанная");
        ItemDto savedItemDto1 = itemService.addNewItem(savedOwner.getId(), itemDto1);

        ItemDto itemDto2 = makeItemDto("Бигуди", "Бигуди для афрокудрей");
        ItemDto savedItemDto2 = itemService.addNewItem(savedOwner.getId(), itemDto2);

        UserDto bookerDto = makeUserDto("Юлия Изюмова", "y_izyumova@email.com");
        UserDto savedBooker = userService.createUser(bookerDto);

        BookingDtoInput bookingDtoInput1 = makeBookingDtoInput(savedItemDto1);
        bookingService.addNewBooking(savedBooker.getId(), bookingDtoInput1);

        BookingDtoInput bookingDtoInput2 = makeBookingDtoInput(savedItemDto2);
        bookingService.addNewBooking(savedBooker.getId(), bookingDtoInput2);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.booker.id = :bookerId", Booking.class);
        List<Booking> bookings = query.setParameter("bookerId", savedBooker.getId()).getResultList();

        assertThat(bookings.size(), equalTo(2));

        Booking foundedbooking1 = bookings.get(0);
        assertThat(foundedbooking1.getId(), notNullValue());
        assertThat(foundedbooking1.getItem().getId(), equalTo(savedItemDto1.getId()));
        assertThat(foundedbooking1.getBooker().getId(), equalTo(savedBooker.getId()));

        Booking foundedbooking2 = bookings.get(1);
        assertThat(foundedbooking2.getId(), notNullValue());
        assertThat(foundedbooking2.getItem().getId(), equalTo(savedItemDto2.getId()));
        assertThat(foundedbooking2.getBooker().getId(), equalTo(savedBooker.getId()));
    }

    private BookingDtoInput makeBookingDtoInput(ItemDto itemDto) {
        return new BookingDtoInput(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                itemDto.getId()
        );
    }

    private UserDto makeUserDto(String name, String email) {
        return new UserDto(
                null,
                name,
                email);
    }

    private ItemDto makeItemDto(String name, String description) {
        return new ItemDto(
                null,
                name,
                description,
                true,
                null);
    }
}