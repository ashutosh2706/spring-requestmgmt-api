package com.wizardform.api.repository;

import com.wizardform.api.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class RequestRepositoryTest {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private PriorityRepository priorityRepository;
    @Autowired
    private FileDetailRepository fileDetailRepository;

    private Request testRequest;
    private User testUser;
    private Status testStatus;
    private Role testRole;
    private Priority testPriority;
    private FileDetail testFileDetail;

    @BeforeEach
    void setUp() {
        testRole = new Role(1, "ROLE_TEST", null);
        testPriority = new Priority(1, "PRIORITY_HIGH", null);
        testStatus = new Status(1, "TEST", null);
        testFileDetail = new FileDetail(1L, "test_file", "checksum");
        roleRepository.save(testRole);
        priorityRepository.save(testPriority);
        statusRepository.save(testStatus);
        fileDetailRepository.save(testFileDetail);
        testUser = new User(1L, "firstname", "lastname", "test@email", "123", true, testRole, null);
        userRepository.save(testUser);
        testRequest = new Request(1L, "test-request", "test-guardian", "1234567890", LocalDate.now(), testUser, testStatus, testPriority, testFileDetail);
        requestRepository.save(testRequest);
    }


    @Test
    void findByRequestId_ShouldReturnRequest_WhenRequestExists() {
        Optional<Request> foundRequest = requestRepository.findByRequestId(1L);
        assertTrue(foundRequest.isPresent());
        assertThat(foundRequest.get().getRequestId()).isEqualTo(testRequest.getRequestId());
        assertThat(foundRequest.get().getTitle()).isEqualTo(testRequest.getTitle());
        assertThat(foundRequest.get().getGuardianName()).isEqualTo(testRequest.getGuardianName());
        assertThat(foundRequest.get().getPhone()).isEqualTo(testRequest.getPhone());
        assertThat(foundRequest.get().getRequestDate()).isEqualTo(testRequest.getRequestDate());
        assertThat(foundRequest.get().getUser().getUserId()).isEqualTo(testRequest.getUser().getUserId());
        assertThat(foundRequest.get().getUser().getEmail()).isEqualTo(testRequest.getUser().getEmail());
        assertThat(foundRequest.get().getUser().getRole().getRoleId()).isEqualTo(testRequest.getUser().getRole().getRoleId());
        assertThat(foundRequest.get().getUser().getRole().getRoleType()).isEqualTo(testRequest.getUser().getRole().getRoleType());
        assertThat(foundRequest.get().getPriority().getPriorityCode()).isEqualTo(testRequest.getPriority().getPriorityCode());
        assertThat(foundRequest.get().getStatus().getStatusCode()).isEqualTo(testRequest.getStatus().getStatusCode());
        assertThat(foundRequest.get().getFileDetail().getFileId()).isEqualTo(testRequest.getFileDetail().getFileId());
        assertThat(foundRequest.get().getFileDetail().getFileName()).isEqualTo(testRequest.getFileDetail().getFileName());
        assertThat(foundRequest.get().getFileDetail().getChecksum()).isEqualTo(testRequest.getFileDetail().getChecksum());
    }

    @Test
    void findByRequestId_ShouldReturnEmpty_WhenRequestDoesNotExist() {
        Optional<Request> foundRequest = requestRepository.findByRequestId(999L);
        assertTrue(foundRequest.isEmpty());
    }

    @AfterEach
    void tearDown() {
        testRole = null;
        testPriority = null;
        testStatus = null;
        testFileDetail = null;
        testUser = null;
        testRequest = null;
        requestRepository.deleteAll();
        userRepository.deleteAll();
        fileDetailRepository.deleteAll();
        roleRepository.deleteAll();
        priorityRepository.deleteAll();
        statusRepository.deleteAll();
    }
}
