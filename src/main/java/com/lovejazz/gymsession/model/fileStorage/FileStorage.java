package com.lovejazz.gymsession.model.fileStorage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class FileStorage {

    private Long id;

    private String keyS3;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private String source;

    private String externalId;

    private LocalDateTime createdAt;
}