package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Comment;

public interface RepositoryComment extends JpaRepository<Comment, Long> {
}
