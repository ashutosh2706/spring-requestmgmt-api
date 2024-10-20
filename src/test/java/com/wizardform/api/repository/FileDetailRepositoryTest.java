package com.wizardform.api.repository;

import com.wizardform.api.model.FileDetail;
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
public class FileDetailRepositoryTest {

    @Autowired
    private FileDetailRepository fileDetailRepository;
    private FileDetail testFileDetail;

    @BeforeEach
    void setUp() {
        testFileDetail = new FileDetail(1L, "testFile.txt", "checksum");
        fileDetailRepository.save(testFileDetail);
    }

    @Test
    void findByFileId_ShouldReturnFileDetail_WhenFileDetailExists() {
        Optional<FileDetail> foundFileDetail = fileDetailRepository.findByFileId(testFileDetail.getFileId());
        assertTrue(foundFileDetail.isPresent());
        assertThat(foundFileDetail.get().getFileId()).isEqualTo(testFileDetail.getFileId());
        assertThat(foundFileDetail.get().getFileName()).isEqualTo(testFileDetail.getFileName());
        assertThat(foundFileDetail.get().getChecksum()).isEqualTo(testFileDetail.getChecksum());
    }

    @Test
    void findByFileId_ShouldReturnEmpty_WhenFileDetailDoesNotExist() {
        Optional<FileDetail> foundFileDetail = fileDetailRepository.findByFileId(999L);
        assertTrue(foundFileDetail.isEmpty());
    }

    @AfterEach
    void tearDown() {
        testFileDetail = null;
        fileDetailRepository.deleteAll();
    }
}
