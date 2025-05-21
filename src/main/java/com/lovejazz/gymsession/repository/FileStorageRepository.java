package com.lovejazz.gymsession.repository;

import com.lovejazz.gymsession.model.fileStorage.FileStorageDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileStorageRepository {
    private final JdbcClient jdbcClient;

    public FileStorageRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void create(FileStorageDTO fileStorage) {
        System.out.println(fileStorage);
        var created = jdbcClient.sql("""
    INSERT INTO file_storage(id, key_s3, file_name, file_type, created_at, updated_at) 
    VALUES (?, ?, ?, ?)
    """)
                .params(List.of(
                        fileStorage.id(),
                        fileStorage.keyS3(),
                        fileStorage.fileName(),
                        fileStorage.fileType()
                ))
                .update();
        Assert.state(created == 1, "Failed to create file storage record for " + fileStorage.fileName());
    }

    public Optional<FileStorageDTO> findById(UUID id) {
        return jdbcClient.sql("SELECT * FROM file_storage WHERE id = :id")
                .param("id", id)
                .query((rs, rowNum) -> new FileStorageDTO(
                    rs.getObject("id", UUID.class),
                    rs.getString("key_s3"),
                    rs.getString("file_name"),
                    rs.getString("file_type")
                ))
                .optional();
    }

    public Optional<FileStorageDTO> findByKeyS3(String keyS3) {
        return jdbcClient.sql("SELECT * FROM file_storage WHERE key_s3 = :keyS3")
                .param("keyS3", keyS3)
                .query((rs, rowNum) -> new FileStorageDTO(
                    rs.getObject("id", UUID.class),
                    rs.getString("key_s3"),
                    rs.getString("file_name"),
                    rs.getString("file_type")
                ))
                .optional();
    }

    public void delete(UUID id) {
        var deleted = jdbcClient.sql("DELETE FROM file_storage WHERE id = :id")
                .param("id", id)
                .update();
        Assert.state(deleted == 1, "Failed to delete file storage record with id " + id);
    }

    public void update(FileStorageDTO fileStorage) {
        var updated = jdbcClient.sql("UPDATE file_storage SET key_s3 = ?, file_name = ?, file_type = ? WHERE id = ?")
                .params(List.of(
                    fileStorage.keyS3(),
                    fileStorage.fileName(),
                    fileStorage.fileType(),
                    fileStorage.id()
                ))
                .update();
        Assert.state(updated == 1, "Failed to update file storage record for " + fileStorage.fileName());
    }
} 