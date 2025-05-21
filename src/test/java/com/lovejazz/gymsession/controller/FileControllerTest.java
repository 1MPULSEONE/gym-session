package com.lovejazz.gymsession.controller;

import com.lovejazz.gymsession.service.MinioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MinioService minioService;

    @Test
    void whenUploadFile_thenReturnsFileName() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, World!".getBytes()
        );
        String fileName = "test-file-name";
        when(minioService.uploadFile(any())).thenReturn(fileName);

        // Act & Assert
        mockMvc.perform(multipart("/api/files/upload")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string(fileName));
    }

    @Test
    void whenDownloadFile_thenReturnsFile() throws Exception {
        // Arrange
        String fileName = "test-file.txt";
        byte[] fileContent = "Hello, World!".getBytes();
        when(minioService.downloadFile(fileName)).thenReturn(fileContent);

        // Act & Assert
        mockMvc.perform(get("/api/files/download/{fileName}", fileName))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName))
                .andExpect(content().bytes(fileContent));
    }

    @Test
    void whenDeleteFile_thenReturnsSuccess() throws Exception {
        // Arrange
        String fileName = "test-file.txt";

        // Act & Assert
        mockMvc.perform(delete("/api/files/{fileName}", fileName))
                .andExpect(status().isOk())
                .andExpect(content().string("File deleted successfully"));
    }

    @Test
    void whenUploadFileFails_thenReturnsError() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, World!".getBytes()
        );
        when(minioService.uploadFile(any())).thenThrow(new RuntimeException("Upload failed"));

        // Act & Assert
        mockMvc.perform(multipart("/api/files/upload")
                .file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to upload file: Upload failed"));
    }
} 