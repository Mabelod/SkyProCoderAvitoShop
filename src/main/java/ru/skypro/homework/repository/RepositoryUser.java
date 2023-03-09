package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.User;

public interface RepositoryUser extends JpaRepository<User, Long> {
}
