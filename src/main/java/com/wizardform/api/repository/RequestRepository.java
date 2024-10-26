package com.wizardform.api.repository;

import com.wizardform.api.model.Request;
import com.wizardform.api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByRequestId(long requestId);
    Page<Request> findByUser(User user, Pageable pageable);
}
