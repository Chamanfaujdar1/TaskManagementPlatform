package com.flowboard.comment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if ("placeholder".equals(accessKey)) {
            return uploadLocally(file);
        }
        
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    private String uploadLocally(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        java.nio.file.Path uploadPath = java.nio.file.Paths.get("uploads");
        
        if (!java.nio.file.Files.exists(uploadPath)) {
            java.nio.file.Files.createDirectories(uploadPath);
        }
        
        java.nio.file.Path filePath = uploadPath.resolve(fileName);
        java.nio.file.Files.copy(file.getInputStream(), filePath);
        
        // Return a local URL that our CommentService will serve
        return "http://localhost:8086/api/v1/attachments/files/" + fileName;
    }

    public void deleteFile(String fileUrl) {
        if ("placeholder".equals(accessKey)) {
            deleteLocally(fileUrl);
            return;
        }
        String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3Client.deleteObject(builder -> builder.bucket(bucketName).key(key));
    }

    private void deleteLocally(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("uploads").resolve(fileName));
        } catch (IOException ignored) {}
    }
}
