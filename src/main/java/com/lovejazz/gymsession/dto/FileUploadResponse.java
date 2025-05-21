package com.lovejazz.gymsession.dto;

import com.lovejazz.gymsession.model.fileStorage.FileStorageDTO;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FileUploadResponse {
    private String uploadUrl;
    private UUID fileId;
    private String fileKey;
    private FileStorageDTO fileRecord;
} 