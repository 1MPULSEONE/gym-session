package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.dto.FileUploadResponse;
import com.lovejazz.gymsession.model.fileStorage.FileStorageDTO;
import com.lovejazz.gymsession.repository.FileStorageRepository;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
    private final MinioClient minioClient;
    private final FileStorageRepository fileStorageRepository;

    @Value("${minio.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws Exception {
        // Генерируем уникальное имя файла
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        // Проверяем существование бакета
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());
        
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }

        // Загружаем файл
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());

        return fileName;
    }

    public byte[] downloadFile(String fileName) throws Exception {
        InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());

        return stream.readAllBytes();
    }

    public void deleteFile(String fileName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
    }

    public FileUploadResponse generateUploadUrl(String fileName, String fileType) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String uniqueKey = UUID.randomUUID() + fileExtension;

        try {
            String uploadUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucketName)
                            .object(uniqueKey)
                            .expiry(1, TimeUnit.HOURS)
                            .build());

            FileStorageDTO fileRecord = new FileStorageDTO(
                UUID.randomUUID(),
                uniqueKey,
                fileName,
                fileType
            );

            fileStorageRepository.create(fileRecord);

            return FileUploadResponse.builder()
                    .uploadUrl(uploadUrl)
                    .fileId(fileRecord.id())
                    .fileKey(uniqueKey)
                    .fileRecord(fileRecord)
                    .build();
        } catch (Exception e) {
            log.error("Failed to generate upload URL", e);
            throw new RuntimeException("File upload error", e);
        }
    }

    public void uploadFile(String uploadUrl, MultipartFile file) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(uploadUrl).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", file.getContentType());

            try (OutputStream out = connection.getOutputStream()) {
                out.write(file.getBytes());
            }

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("File upload failed");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public void updateFileRecord(FileStorageDTO fileRecord) {
        fileStorageRepository.update(fileRecord);
    }
} 