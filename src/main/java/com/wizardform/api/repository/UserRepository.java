package com.wizardform.api.repository;

import com.wizardform.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // we can use annotation @Query("...") to write complex queries with custom results, which are not by default supported by jpa
    Optional<User> findByUserId(Long userId);
    Optional<User> findByEmail(String email);
}
