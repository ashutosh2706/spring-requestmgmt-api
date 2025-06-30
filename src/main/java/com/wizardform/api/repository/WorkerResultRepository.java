package com.wizardform.api.repository;

import com.wizardform.api.model.worker.WorkerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerResultRepository extends JpaRepository<WorkerResult, Integer> {
    Optional<WorkerResult> findByResultId(int resultId);
}
