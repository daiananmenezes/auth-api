package com.mnz.authapi.repository;

import com.mnz.authapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryJPA extends JpaRepository<User, Long> {
        Optional<User> findByUsername(String username);
}
