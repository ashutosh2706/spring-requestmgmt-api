package com.wizardform.api.repository;

import com.wizardform.api.model.ExceptionDbLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionLogRepository extends JpaRepository<ExceptionDbLog, Long> {
}
