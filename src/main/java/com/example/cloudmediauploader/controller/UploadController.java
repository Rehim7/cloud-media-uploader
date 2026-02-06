package com.example.cloudmediauploader.controller;

import com.example.cloudmediauploader.model.ProfileImage;
import com.example.cloudmediauploader.model.UploadDto;
import com.example.cloudmediauploader.repository.ProfileImageRepository;
import com.example.cloudmediauploader.servic.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UploadController {
    private final StorageService storageService;
    private final ProfileImageRepository profileImageRepository;


    @PostMapping("/upload")
    public UploadDto upload(@RequestParam("file") MultipartFile file) {
        try {
            String upload = storageService.upload(file);

            ProfileImage profileImage = ProfileImage.builder()
                    .fileName(file.getName())
                    .fileUrl(upload)
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .build();

            ProfileImage save = profileImageRepository.save(profileImage);
            UploadDto uploadDto = UploadDto.builder()
                    .id(save.getId())
                    .fileUrl(save.getFileUrl())
                    .fileName(save.getFileName())
                    .contentType(save.getContentType())
                    .fileSize(save.getFileSize())
                    .uploadedAt(save.getUploadedAt())
                    .build();
            return uploadDto;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAllImages")
    public List<UploadDto> getAllImages() {
        return profileImageRepository.findAll().stream().map(image -> UploadDto.builder()
                .id(image.getId())
                .fileUrl(image.getFileUrl())
                .fileName(image.getFileName())
                .contentType(image.getContentType())
                .fileSize(image.getFileSize())
                .uploadedAt(image.getUploadedAt())
                .build()).toList();
    }


}
