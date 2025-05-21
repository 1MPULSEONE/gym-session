package com.lovejazz.gymsession.model.fileStorage;

import java.util.UUID;

public record FileStorageDTO(
    UUID id,
    String keyS3,
    String fileName,
    String fileType
) {} 