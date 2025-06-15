package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // Аннотированный @Query метод (JPQL)
    @Query("""
            SELECT i FROM Item i WHERE
            (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) OR
            UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))) AND
            i.available = true""")
    List<Item> getSearch(String text);

    // Запросный метод (Query Method)
    List<Item> findByOwnerId(long userId);

    //     Запросный метод (Query Method)
    List<Item> findByRequestId(long requestId);
}