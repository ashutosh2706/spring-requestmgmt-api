package com.wizardform.api.service.impl;

import com.wizardform.api.dto.NewRequestDto;
import com.wizardform.api.exception.FileDetailsNotFoundException;
import com.wizardform.api.helper.Utils;
import com.wizardform.api.model.FileDetail;
import com.wizardform.api.repository.FileDetailRepository;
import com.wizardform.api.service.FileService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import static com.wizardform.api.Constants.ROOT_PATH;
import static com.wizardform.api.Constants.UPLOAD_DIR;

@Component
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileDetailRepository fileDetailRepository;

    @Autowired
    public FileServiceImpl(FileDetailRepository fileDetailRepository) {
        this.fileDetailRepository = fileDetailRepository;
    }

    /**
     *
     * TODO: Make this saveFile() method asynchronous
     * TODO: Optimize for multiple upload request at single time
     */
    @Override
    public FileDetail saveFile(MultipartFile file) throws IOException {

        File uploadDir = new File(ROOT_PATH, UPLOAD_DIR);
        if(!uploadDir.exists()) uploadDir.mkdir();

        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.') + 1) : "";
        String newFileName = String.format("Attachment_%d.%s", System.currentTimeMillis(), extension);
        File destinationFile = new File(uploadDir, newFileName);
        file.transferTo(destinationFile);

        String fileChecksum = Utils.calculateChecksum(destinationFile);

        // Create a FileDetail instance and save it to DB
        FileDetail fileDetail = new FileDetail();
        fileDetail.setFileName(newFileName);
        fileDetail.setChecksum(fileChecksum);
        return fileDetailRepository.save(fileDetail);
    }

    @Override
    public FileDetail saveFile(File tmpFile) throws IOException {

        File uploadDir = new File(ROOT_PATH, UPLOAD_DIR);
        if(!uploadDir.exists()) uploadDir.mkdir();

        String originalFileName = tmpFile.getName();
        String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.') + 1) : "";
        String newFileName = String.format("Attachment_%d.%s", System.currentTimeMillis(), extension);
        File destinationFile = new File(uploadDir, newFileName);
        Files.copy(tmpFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        String fileChecksum = Utils.calculateChecksum(destinationFile);
        tmpFile.delete();
        // Create a FileDetail instance and save it to DB
        FileDetail fileDetail = new FileDetail();
        fileDetail.setFileName(newFileName);
        fileDetail.setChecksum(fileChecksum);
        return fileDetailRepository.save(fileDetail);
    }

    @Override
    public FileDetail getFileDetailByFileId(long fileId) throws FileDetailsNotFoundException {
        Optional<FileDetail> fileDetailsOptional = fileDetailRepository.findByFileId(fileId);
        if(fileDetailsOptional.isPresent()) {
            return fileDetailsOptional.get();
        } else {
            log.error("FileDetailsNotFoundException: No file details found for fileId {}", fileId);
            throw new FileDetailsNotFoundException("No file details found for fileId: " + fileId);
        }
    }

    @Override
    @Transactional
    public void deleteFileDetails(long fileId) throws FileDetailsNotFoundException {
        Optional<FileDetail> fileDetailsOptional = fileDetailRepository.findByFileId(fileId);
        if(fileDetailsOptional.isPresent()) {
            fileDetailRepository.delete(fileDetailsOptional.get());
            // delete attachment (if present)
            String fileName = fileDetailsOptional.get().getFileName();
            File attachment = new File(ROOT_PATH, UPLOAD_DIR + File.separator + fileName);
            if(attachment.exists()) {
                attachment.delete();
            }
        } else {
            log.error("FileDetailsNotFoundException: No file details found for fileId {}", fileId);
            throw new FileDetailsNotFoundException("No file details found for fileId: " + fileId);
        }
    }

    @Override
    public File createTmpFile(NewRequestDto newRequestDto) {
        if(newRequestDto.getAttachedFile() != null) {
            var incoming = newRequestDto.getAttachedFile();
            File tmpdir = new File(ROOT_PATH, "tmp");
            File tmpFile = new File(tmpdir, incoming.getOriginalFilename());
            if(!tmpdir.exists()) tmpdir.mkdir();
            try {
                incoming.transferTo(tmpFile);
            } catch (IOException e) {
                return null;
            }
            return tmpFile;
        }
        return null;
    }
}
