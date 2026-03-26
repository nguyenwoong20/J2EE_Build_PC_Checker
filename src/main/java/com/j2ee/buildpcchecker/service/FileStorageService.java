package com.j2ee.buildpcchecker.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class FileStorageService {

    final Cloudinary cloudinary;

    @Value("${app.cloudinary.folder}")
    String cloudinaryFolder;

    public String storeFile(MultipartFile file) {
        return storeFile(file, "image");
    }

    public String storeFile(MultipartFile file, String entity) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }

        String safeEntity = normalizeEntity(entity);
        String folder = normalizeFolder(cloudinaryFolder);
        String fullFolder = folder.isEmpty() ? safeEntity : folder + "/" + safeEntity;
        String publicId = safeEntity + "-" + UUID.randomUUID();

        Map<String, Object> options = new HashMap<>();
        options.put("public_id", publicId);
        options.put("folder", fullFolder);
        options.put("resource_type", "image");

        try {
            Uploader uploader = cloudinary.uploader();
            Map<?, ?> uploadResult = uploader.upload(file.getBytes(), options);
            String secureUrl = (String) uploadResult.get("secure_url");
            if (!StringUtils.hasText(secureUrl)) {
                log.error("Cloudinary upload succeeded without secure_url. Result keys: {}", uploadResult.keySet());
                throw new AppException(ErrorCode.FILE_STORAGE_ERROR);
            }
            return secureUrl;
        } catch (IOException e) {
            log.error("Cloudinary upload failed: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.FILE_STORAGE_ERROR);
        }
    }

    private String normalizeEntity(String entity) {
        if (!StringUtils.hasText(entity)) {
            return "image";
        }

        String normalized = entity.trim();
        if (!normalized.matches("[A-Za-z0-9_-]+")) {
            throw new AppException(ErrorCode.FILE_INVALID_NAME);
        }

        return normalized;
    }

    private String normalizeFolder(String folder) {
        if (!StringUtils.hasText(folder)) {
            return "";
        }

        String normalized = folder.trim();
        if (!normalized.matches("[A-Za-z0-9/_-]+")) {
            throw new AppException(ErrorCode.FILE_INVALID_NAME);
        }

        normalized = normalized.replaceAll("^/+", "");
        normalized = normalized.replaceAll("/+$", "");
        return normalized;
    }
}
