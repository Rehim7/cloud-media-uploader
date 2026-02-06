package com.example.cloudmediauploader.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadDto {
    private Long id;

    private String fileName;

    private String fileUrl;

    private String contentType;

    private Long fileSize;

    private LocalDateTime uploadedAt;
}
