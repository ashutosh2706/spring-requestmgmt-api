package com.wizardform.api.repository;

import com.wizardform.api.model.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class StatusRepositoryTest {

    @Autowired
    private StatusRepository statusRepository;
    private Status testStatus;

    @BeforeEach
    void setUp() {
        testStatus = new Status(1, "test_status", null);
        statusRepository.save(testStatus);
    }

    @Test
    void findByStatusCode_ShouldReturnStatus_WhenStatusExists() {
        Optional<Status> foundStatus = statusRepository.findByStatusCode(1);
        assertTrue(foundStatus.isPresent());
        assertThat(foundStatus.get().getStatusCode()).isEqualTo(testStatus.getStatusCode());
        assertThat(foundStatus.get().getDescription()).isEqualTo(testStatus.getDescription());
    }

    @Test
    void findByStatusCode_ShouldReturnEmpty_WhenStatusDoesNotExist() {
        Optional<Status> foundStatus = statusRepository.findByStatusCode(999);
        assertTrue(foundStatus.isEmpty());
    }

    @AfterEach
    void tearDown() {
        testStatus = null;
        statusRepository.deleteAll();
    }

}
