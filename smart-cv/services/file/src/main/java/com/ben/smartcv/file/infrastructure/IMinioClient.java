package com.ben.smartcv.file.infrastructure;

import java.io.File;

public interface IMinioClient {

    void storeObject(File file, String fileName, String contentType, String bucketName);

    String getObjectUrl(String objectKey);

    void deleteObject(String objectKey, String bucketName);

}
