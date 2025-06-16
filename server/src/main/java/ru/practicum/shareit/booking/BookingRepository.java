package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
            ORDER BY b.start DESC
            """)
    List<Booking> getAllUserBookings(@Param("userId") Long userId);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
            AND b.status = 'APPROVED'
            AND :localDateTime >= b.start AND :localDateTime <= b.end
            ORDER BY b.start DESC""")
    List<Booking> getCurrentUserBookings(@Param("userId") long userId,
                                         @Param("localDateTime") LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
            AND b.status = 'APPROVED'
            AND :localDateTime < b.start
            ORDER BY b.start DESC""")
    List<Booking> getFutureUserBookings(@Param("userId") long userId,
                                        @Param("localDateTime") LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
            AND b.status = 'APPROVED'
            AND :localDateTime > b.end
            ORDER BY b.start DESC""")
    List<Booking> getPastUserBookings(@Param("userId") long userId,
                                      @Param("localDateTime") LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
            AND b.status = 'WAITING'
            ORDER BY b.start DESC""")
    List<Booking> getWaitingUserBookings(@Param("userId") long userId);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
            AND b.status = 'REJECTED'
            ORDER BY b.start DESC""")
    List<Booking> getRejectedUserBookings(@Param("userId") long userId);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
            ORDER BY b.start DESC""")
    List<Booking> getAllItemBookings(@Param("itemId") Long itemId);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
            AND b.status = 'APPROVED'
            AND :localDateTime >= b.start AND :localDateTime <= b.end
            ORDER BY b.start DESC""")
    List<Booking> getCurrentItemBookings(@Param("itemId") long itemId,
                                         @Param("localDateTime") LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
            AND b.status = 'APPROVED'
            AND :localDateTime < b.start
            ORDER BY b.start DESC""")
    List<Booking> getFutureItemBookings(@Param("itemId") long itemId,
                                        @Param("localDateTime") LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
            AND b.status = 'APPROVED'
            AND :localDateTime > b.end
            ORDER BY b.start DESC""")
    List<Booking> getPastItemBookings(@Param("itemId") long itemId,
                                      @Param("localDateTime") LocalDateTime localDateTime);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
            AND b.status = 'WAITING'
            ORDER BY b.start DESC""")
    List<Booking> getWaitingItemBookings(@Param("itemId") long itemId);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
            AND b.status = 'REJECTED'
            ORDER BY b.start DESC""")
    List<Booking> getRejectedItemBookings(@Param("itemId") long itemId);
}

