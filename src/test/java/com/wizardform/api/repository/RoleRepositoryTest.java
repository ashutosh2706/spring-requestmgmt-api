package com.wizardform.api.repository;

import com.wizardform.api.model.Role;
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
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    private Role testRole;

    // Arrange
    @BeforeEach
    void setUp() {
        testRole = new Role(1,"ROLE_TEST", null);
        roleRepository.save(testRole);
    }

    @Test
    void findByRoleId_ShouldReturnRole_WhenRoleExists() {
        // Act
        Optional<Role> foundRole = roleRepository.findByRoleId(1);
        // Assert
        assertTrue(foundRole.isPresent());
        assertThat(foundRole.get().getRoleId()).isEqualTo(testRole.getRoleId());
        assertThat(foundRole.get().getRoleType()).isEqualTo(testRole.getRoleType());
    }

    @Test
    void findByRoleId_ShouldReturnEmpty_WhenRoleDoesNotExist() {
        Optional<Role> foundRole = roleRepository.findByRoleId(999);
        assertTrue(foundRole.isEmpty());
    }

    @AfterEach
    void tearDown() {
        testRole = null;
        roleRepository.deleteAll();
    }
}
