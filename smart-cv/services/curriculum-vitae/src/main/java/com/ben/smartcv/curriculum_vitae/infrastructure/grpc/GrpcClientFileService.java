package com.ben.smartcv.curriculum_vitae.infrastructure.grpc;

import com.ben.smartcv.common.file.DownloadUrlEntry;
import com.ben.smartcv.common.file.DownloadUrls;
import com.ben.smartcv.common.file.FileServiceGrpc;
import com.ben.smartcv.common.file.ObjectKeys;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.grpc.ManagedChannel;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GrpcClientFileService {

    //ManagedChannel fileServiceManagedChannel;

    FileServiceGrpc.FileServiceBlockingStub fileServiceClient;

    @CircuitBreaker(name = "fileService", fallbackMethod = "fallback")
    @TimeLimiter(name = "fileService")
    public CompletableFuture<Map<String, String>> callGetAllDownloadUrls(List<String> objectKeys) {
        return CompletableFuture.supplyAsync(() -> {
            DownloadUrls response = fileServiceClient.getAllDownloadUrls(
                    ObjectKeys.newBuilder().addAllValues(objectKeys).build());

            return response.getValuesList().stream()
                    .collect(Collectors.toMap(DownloadUrlEntry::getObjectKey, DownloadUrlEntry::getUrl));
        });
    }

    public CompletableFuture<Map<String, String>> fallback(Throwable t) {
        log.warn("Fallback triggered due to: {}", t.getMessage());
        return CompletableFuture.completedFuture(Collections.emptyMap());
    }

//    @PreDestroy
//    public void shutdownGrpcChanel() {
//        fileServiceManagedChannel.shutdown();
//    }

}
