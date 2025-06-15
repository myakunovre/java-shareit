package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    //     Аннотированный @Query метод (JPQL)
    @Query("""
            SELECT ir
            FROM ItemRequest ir
            WHERE ir.requestor.id = ?1
            ORDER BY ir.created DESC
            """)
    List<ItemRequest> getUserItemRequests(long userId);

    @Query("""
            SELECT ir
            FROM ItemRequest ir
            WHERE ir.requestor.id != ?1
            ORDER BY ir.created DESC
            """)
    List<ItemRequest> getAllItemRequests(long userId);
}

