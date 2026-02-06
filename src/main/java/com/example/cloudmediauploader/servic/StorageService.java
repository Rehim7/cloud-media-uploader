package com.example.cloudmediauploader.servic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@Service
public class StorageService {

    private final S3Client s3Client;

    @Value("${digitalocean.spaces.bucket}")
    private  String bucketName;

    @Value("${digitalocean.spaces.endpoint}")
    private String endpoint;

    @Value("${upload.allowed-types}")
    private String allowedTypes;
    public StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    private void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String contentType = file.getContentType();
        List<String> allowedTypesList = Arrays.asList(allowedTypes.split(","));
        if (!allowedTypesList.contains(contentType)) {
            throw new IllegalArgumentException("Invalid content type");
        }
    }
    public String upload(MultipartFile file) throws IOException {
        validate(file);

        String fileName = file.getOriginalFilename();
        String extension = getFileExtension(fileName);
        String uniqueFileName = UUID.randomUUID().toString()  + extension;
        try {


            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .contentType(file.getContentType())
                    .acl("public-read")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String url = String.format("%s/%s/%s", endpoint, bucketName, uniqueFileName);
            return url;
        }catch (S3Exception e){
            throw new RuntimeException(e);

        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }


}
