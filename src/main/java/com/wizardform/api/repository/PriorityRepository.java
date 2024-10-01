package com.wizardform.api.repository;

import com.wizardform.api.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Integer> {
    Optional<Priority> findByPriorityCode(int priorityCode);
}
