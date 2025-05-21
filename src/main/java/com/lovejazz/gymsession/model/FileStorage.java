package com.lovejazz.gymsession.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileStorage {
    private UUID id;
    private String keyS3;
    private String fileName;
    private String fileType;
    private String source;
    private String externalId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}