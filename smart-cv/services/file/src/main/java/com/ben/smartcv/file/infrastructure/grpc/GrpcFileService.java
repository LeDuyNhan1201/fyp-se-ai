package com.ben.smartcv.file.infrastructure.grpc;

import com.ben.smartcv.common.file.DownloadUrlEntry;
import com.ben.smartcv.common.file.DownloadUrls;
import com.ben.smartcv.common.file.FileServiceGrpc;
import com.ben.smartcv.common.file.ObjectKeys;
import com.ben.smartcv.file.infrastructure.minio.IMinioClient;
import io.grpc.protobuf.services.HealthStatusManager;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GrpcFileService extends FileServiceGrpc.FileServiceImplBase {

    IMinioClient minioClient;

    @Override
    public void getAllDownloadUrls(ObjectKeys request, StreamObserver<DownloadUrls> responseObserver) {
        List<DownloadUrlEntry> entries = request.getValuesList().stream()
                .map(objectKey -> {
                    String url = minioClient.getObjectUrl(objectKey);
                    return url != null
                            ? DownloadUrlEntry.newBuilder()
                            .setObjectKey(objectKey)
                            .setUrl(url)
                            .build()
                            : null;
                })
                .filter(Objects::nonNull)
                .toList();

        DownloadUrls response = DownloadUrls.newBuilder()
                .addAllValues(entries)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
