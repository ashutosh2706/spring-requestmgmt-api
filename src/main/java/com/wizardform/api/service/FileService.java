package com.wizardform.api.service;

import com.wizardform.api.exception.FileDetailsNotFoundException;
import com.wizardform.api.model.FileDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {
    FileDetail saveFile(MultipartFile file) throws IOException;
    FileDetail getFileDetailByFileId(long fileId) throws FileDetailsNotFoundException;
    void deleteFileDetails(long fileId) throws FileDetailsNotFoundException;
}
