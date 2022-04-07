package com.example.next_level_technologies.repositories;

import com.example.next_level_technologies.entities.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);
}
