package com.ben.smartcv.file.infrastructure.minio;

import com.ben.smartcv.file.application.exception.FileError;
import com.ben.smartcv.file.application.exception.FileHttpException;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MinioClientImpl implements IMinioClient {

    MinioClient minioClient;

    @Value("${minio.bucket-name}")
    @NonFinal
    String bucketName;

    public MinioClientImpl(@Value("${minio.endpoint}") String endpoint,
                                  @Value("${minio.access-key}") String accessKey,
                                  @Value("${minio.secret-key}") String secretKey) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @PostConstruct
    public void initBucket() {
        try {
            ensureBucketExists(bucketName);

        } catch (Exception e) {
            log.error("Error initializing bucket", e);
            throw new FileHttpException(FileError.CAN_NOT_INIT_BUCKET, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void storeObject(File file, String fileName, String contentType, String bucketName)
            throws FileHttpException {
        try {
            ensureBucketExists(bucketName);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(Files.newInputStream(file.toPath()), file.length(), -1)
                    .contentType(contentType)
                    .build());

        } catch (Exception e) {
            log.error("Error storing object", e);
            throw new FileHttpException(FileError.CAN_NOT_STORE_FILE, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String getObjectUrl(String objectKey) throws FileHttpException {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectKey)
                    .expiry(1, TimeUnit.DAYS)
                    .build());

        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Error getting object URL", e);
            throw new FileHttpException(FileError.COULD_NOT_READ_FILE, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void deleteObject(String objectKey, String bucketName) throws FileHttpException {
        GetObjectResponse response;
        try {
            response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectKey)
                    .build());
        } catch (Exception e) {
            log.error("Error getting object", e);
            throw new FileHttpException(FileError.COULD_NOT_READ_FILE, HttpStatus.BAD_REQUEST);
        }

        if (response == null) throw new FileHttpException(FileError.FILE_NOT_FOUND, HttpStatus.BAD_REQUEST);

        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectKey)
                    .build());

        } catch (Exception e) {
            log.error("Error deleting object", e);
            throw new FileHttpException(FileError.CAN_NOT_DELETE_FILE, HttpStatus.BAD_REQUEST);
        }
    }

    private void ensureBucketExists(String bucketName) {
        boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket '{}' created.", bucketName);
            }
        } catch (Exception e) {
            log.error("Error checking bucket existence", e);
            throw new FileHttpException(FileError.CAN_NOT_CHECK_BUCKET, HttpStatus.BAD_REQUEST);
        }
    }

}
