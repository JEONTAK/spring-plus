package org.example.expert.domain.common.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.ServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private final S3Client s3Client;

    @Getter
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String upload(MultipartFile multipartFile) {

        String originalFilename = multipartFile.getOriginalFilename();

        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new InvalidRequestException("지원되지 않는 파일 형식입니다. 허용 확장자: " + ALLOWED_EXTENSIONS);
        }

        if (multipartFile.getSize() > MAX_FILE_SIZE) {
            throw new InvalidRequestException("파일 크기가 5MB를 초과했습니다. 최대 크기: " + MAX_FILE_SIZE / (1024 * 1024) + "MB");
        }

        String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .contentLength(multipartFile.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                    multipartFile.getInputStream(), multipartFile.getSize()));
            log.info("파일 업로드 성공: bucket={}, key={}", bucket, fileName);
            return getPublicUrl(fileName);
        } catch (IOException e) {
            log.error("파일 업로드 실패: bucket={}, key={}, cause={}", bucket, fileName, e.getMessage(), e);
            throw new ServerException("파일 업로드 중 서버 오류가 발생했습니다: " + e.getMessage());
        }
    }


    public void delete(String objectPath) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectPath)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            log.info("파일 삭제 성공: bucket={}, key={}", bucket, objectPath);
        } catch (SdkServiceException e) {
            log.error("파일 삭제 실패: bucket={}, key={}, cause={}", bucket, objectPath, e.getMessage(), e);
            throw new ServerException("파일 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new InvalidRequestException("유효한 파일 확장자가 없습니다.");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}