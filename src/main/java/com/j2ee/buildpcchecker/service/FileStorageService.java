package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FileStorageService {

    @Value("${app.file.upload-dir:/uploads/images}")
    String uploadDir;

    public String storeFile(MultipartFile file) {
        return storeFile(file, "image");
    }

    public String storeFile(MultipartFile file, String entity) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }

        String safeEntity = normalizeEntity(entity);
        String originalName = StringUtils.cleanPath(StringUtils.hasText(file.getOriginalFilename())
                ? file.getOriginalFilename()
                : "");

        String extension = extractExtension(originalName);
        String filename = safeEntity + "-" + UUID.randomUUID();
        if (StringUtils.hasText(extension)) {
            filename = filename + "." + extension;
        }

        Path targetDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(targetDir);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_STORAGE_ERROR);
        }

        Path targetPath = targetDir.resolve(filename).normalize();
        if (!targetPath.startsWith(targetDir)) {
            throw new AppException(ErrorCode.FILE_INVALID_NAME);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_STORAGE_ERROR);
        }

        return filename;
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

    private String extractExtension(String originalName) {
        int lastDotIndex = originalName.lastIndexOf('.');
        if (lastDotIndex <= 0 || lastDotIndex == originalName.length() - 1) {
            return "";
        }

        return originalName.substring(lastDotIndex + 1);
    }
}

