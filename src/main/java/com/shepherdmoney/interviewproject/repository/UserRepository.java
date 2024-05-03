package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository to store users.
 */
@Repository("UserRepo")
public interface UserRepository extends JpaRepository<User, Integer> {
    // Method to find a user by their email address
    Optional<User> findByEmail(String email);
}
