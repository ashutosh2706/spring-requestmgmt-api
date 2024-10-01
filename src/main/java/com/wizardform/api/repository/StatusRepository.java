package com.wizardform.api.repository;

import com.wizardform.api.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    Optional<Status> findByStatusCode(int statusCode);
}
