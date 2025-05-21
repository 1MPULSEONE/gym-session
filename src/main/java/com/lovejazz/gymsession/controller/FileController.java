package com.lovejazz.gymsession.controller;

import com.lovejazz.gymsession.dto.FileUploadResponse;
import com.lovejazz.gymsession.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileController {
    private final MinioService minioService;

    @PostMapping("/generate-upload-url")
    public ResponseEntity<FileUploadResponse> generateUploadUrl(
            @RequestParam String fileName,
            @RequestParam String fileType) {
        try {
            FileUploadResponse response = minioService.generateUploadUrl(fileName, fileType);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam String uploadUrl,
            @RequestParam("file") MultipartFile file) {
        try {
            minioService.uploadFile(uploadUrl, file);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to upload file: " + e.getMessage());
        }
    }
} 