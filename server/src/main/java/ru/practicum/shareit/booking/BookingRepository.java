package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    //     Аннотированный @Query метод (JPQL)
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1
            ORDER BY b.start DESC
            """)
    List<Booking> getAllUserBookings(long userId);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1
            AND b.status = 'APPROVED'
            AND ?2 >= b.start AND ?2 <= b.end
            ORDER BY b.start DESC""")
    List<Booking> getCurrentUserBookings(long userId, LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1
            AND b.status = 'APPROVED'
            AND ?2 < b.start
            ORDER BY b.start DESC""")
    List<Booking> getFutureUserBookings(long userId, LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1
            AND b.status = 'APPROVED'
            AND ?2 > b.end
            ORDER BY b.start DESC""")
    List<Booking> getPastUserBookings(long userId, LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1
            AND b.status = 'WAITING'
            ORDER BY b.start DESC""")
    List<Booking> getWaitingUserBookings(long userId);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1
            AND b.status = 'REJECTED'
            ORDER BY b.start DESC""")
    List<Booking> getRejectedUserBookings(long userId);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = ?1
            ORDER BY b.start DESC""")
    List<Booking> getAllItemBookings(long itemId);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = ?1
            AND b.status = 'APPROVED'
            AND ?2 >= b.start AND ?2 <= b.end
            ORDER BY b.start DESC""")
    List<Booking> getCurrentItemBookings(long itemId, LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = ?1
            AND b.status = 'APPROVED'
            AND ?2 < b.start
            ORDER BY b.start DESC""")
    List<Booking> getFutureItemBookings(long itemId, LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = ?1
            AND b.status = 'APPROVED'
            AND ?2 > b.end
            ORDER BY b.start DESC""")
    List<Booking> getPastItemBookings(long itemId, LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = ?1
            AND b.status = 'WAITING'
            ORDER BY b.start DESC""")
    List<Booking> getWaitingItemBookings(long itemId);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = ?1
            AND b.status = 'REJECTED'
            ORDER BY b.start DESC""")
    List<Booking> getRejectedItemBookings(long itemId);
}

