package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("""
            SELECT ir
            FROM ItemRequest ir
            WHERE ir.requestor.id = :userId
            ORDER BY ir.created DESC
            """)
    List<ItemRequest> getUserItemRequests(@Param("userId") long userId);

    @Query("""
            SELECT ir
            FROM ItemRequest ir
            WHERE ir.requestor.id != :userId
            ORDER BY ir.created DESC
            """)
    List<ItemRequest> getAllItemRequests(@Param("userId") long userId);
}