package com.wizardform.api.repository;

import com.wizardform.api.model.Priority;
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
public class PriorityRepositoryTest {

    @Autowired
    private PriorityRepository priorityRepository;
    private Priority testPriority;

    @BeforeEach
    void setUp() {
        testPriority = new Priority(1, "Test_Priority", null);
        priorityRepository.save(testPriority);
    }

    @Test
    void findByPriorityCode_ShouldReturnPriority_WhenPriorityExists() {
        Optional<Priority> foundPriority = priorityRepository.findByPriorityCode(testPriority.getPriorityCode());
        assertTrue(foundPriority.isPresent());
        assertThat(foundPriority.get().getPriorityCode()).isEqualTo(testPriority.getPriorityCode());
        assertThat(foundPriority.get().getDescription()).isEqualTo(testPriority.getDescription());
    }

    @Test
    void findByPriorityCode_ShouldReturnEmpty_WhenPriorityDoesNotExist() {
        Optional<Priority> foundPriority = priorityRepository.findByPriorityCode(999);
        assertTrue(foundPriority.isEmpty());
    }

    @AfterEach
    void tearDown() {
        testPriority = null;
        priorityRepository.deleteAll();
    }
}
