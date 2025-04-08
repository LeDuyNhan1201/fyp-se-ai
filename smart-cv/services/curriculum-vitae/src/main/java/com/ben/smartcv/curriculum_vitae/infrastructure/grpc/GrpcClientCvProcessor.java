package com.ben.smartcv.curriculum_vitae.infrastructure.grpc;

import com.ben.smartcv.common.contract.event.CvEvent;
import com.ben.smartcv.common.cv.CvProcessedEvent;
import com.ben.smartcv.common.cv.CvProcessorGrpc;
import com.ben.smartcv.common.cv.ExtractedCvData;
import io.grpc.ManagedChannel;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GrpcClientCvProcessor {

    ManagedChannel cvProcessorManagedChannel;

    CvProcessorGrpc.CvProcessorBlockingStub cvProcessorClient;

    public ExtractedCvData callExtractData(CvEvent.CvProcessed event) {
        CvProcessedEvent protoEvent = CvProcessedEvent.newBuilder()
                .setCvId(event.getCvId())
                .setObjectKey(event.getObjectKey())
                .build();
        ExtractedCvData response = cvProcessorClient.extractData(protoEvent);
        log.info("Extracted cv data: {}", response);
        return response;
    }

    @PreDestroy
    public void shutdownGrpcChanel() {
        cvProcessorManagedChannel.shutdown();
    }

}
