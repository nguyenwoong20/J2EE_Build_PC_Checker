package com.j2ee.buildpcchecker.controller;

import com.j2ee.buildpcchecker.dto.response.UploadFileResponse;
import com.j2ee.buildpcchecker.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FileUploadController {

    final FileStorageService fileStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponse uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "entity", defaultValue = "image") String entity) {
        String url = fileStorageService.storeFile(file, entity);
        return new UploadFileResponse(url);
    }
}
