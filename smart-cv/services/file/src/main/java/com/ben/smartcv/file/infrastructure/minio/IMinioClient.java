package com.ben.smartcv.file.infrastructure.minio;

import com.ben.smartcv.file.application.exception.FileHttpException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public interface IMinioClient {

    void storeObject(File file, String fileName, String contentType, String bucketName) throws FileHttpException;

    String getObjectUrl(String objectKey) throws FileHttpException;

    void deleteObject(String objectKey, String bucketName) throws FileHttpException;

}
