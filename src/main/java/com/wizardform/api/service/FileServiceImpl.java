package com.wizardform.api.service;

import com.wizardform.api.exception.FileDetailsNotFoundException;
import com.wizardform.api.helper.Utils;
import com.wizardform.api.model.FileDetail;
import com.wizardform.api.repository.FileDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.wizardform.api.Constants.ROOT_PATH;
import static com.wizardform.api.Constants.UPLOAD_DIR;

@Component
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
    public FileDetail getFileDetailByFileId(long fileId) throws FileDetailsNotFoundException {
        Optional<FileDetail> fileDetailsOptional = fileDetailRepository.findByFileId(fileId);
        if(fileDetailsOptional.isPresent()) {
            return fileDetailsOptional.get();
        } else throw new FileDetailsNotFoundException("No file details found for fileId: " + fileId);
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
        } else throw new FileDetailsNotFoundException("No file details found for fileId: " + fileId);
    }
}
