package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Запросный метод (Query Method)
    List<Comment> findAllByItemId(long itemId);
}

