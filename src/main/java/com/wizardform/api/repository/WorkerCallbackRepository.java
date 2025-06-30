package com.wizardform.api.repository;

import com.wizardform.api.model.worker.WorkerCallback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerCallbackRepository extends JpaRepository<WorkerCallback, Long> {

}
