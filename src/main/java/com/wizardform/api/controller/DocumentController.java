package com.wizardform.api.controller;

import com.wizardform.api.exception.FileDetailsNotFoundException;
import com.wizardform.api.helper.Utils;
import com.wizardform.api.model.FileDetail;
import com.wizardform.api.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wizardform.api.Constants.UPLOAD_DIR;

@Tag(name = "Document Controller", description = "Handles document download")
@RestController
@RequestMapping("api/document")
public class DocumentController {

    private final FileService fileService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public DocumentController(FileService fileService, ResourceLoader resourceLoader) {
        this.fileService = fileService;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping(value = "download/{documentId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> downloadDocument(@PathVariable long documentId)
            throws FileDetailsNotFoundException {

        FileDetail fileDetail = fileService.getFileDetailByFileId(documentId);
        String filePath = UPLOAD_DIR + "/" + fileDetail.getFileName();
        Resource resource = resourceLoader.getResource("file:" + filePath);
        // check existence of resource on disk
        if(!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        String contentType = Utils.getContentType(resource.getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
