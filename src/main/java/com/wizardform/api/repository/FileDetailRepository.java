package com.wizardform.api.repository;

import com.wizardform.api.model.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailRepository extends JpaRepository<FileDetail, Long> {

}
