package com.krets.auth.repository;

import com.krets.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their username.
     * @param username the username to search for.
     * @return an Optional containing the user if found, or empty otherwise.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists with the given username.
     * @param username the username to check.
     * @return true if a user with that username exists, false otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user exists with the given email address.
     * @param email the email to check.
     * @return true if a user with that email exists, false otherwise.
     */
    Boolean existsByEmail(String email);
}