package com.ben.smartcv.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class FileHelper {

    public static String generateFileName(String fileType, String extension) {
        // Lấy thời gian hiện tại với format yyyyMMdd_HHmmss
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(dateTimeFormatter);

        // Tạo UUID duy nhất
        String uniqueId = UUID.randomUUID().toString();

        // Kết hợp tên file
        return String.format("%s_%s_%s.%s", fileType, timestamp, uniqueId, extension);
    }

    public static File convertToFile(MultipartFile multipartFile) throws IOException {
        // Tạo file tạm
        File tempFile = File.createTempFile("upload-", "-" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);
        return tempFile;
    }

}
