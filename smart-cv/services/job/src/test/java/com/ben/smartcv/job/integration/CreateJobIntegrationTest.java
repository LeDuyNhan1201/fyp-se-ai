package com.ben.smartcv.job.integration;

import com.ben.smartcv.common.contract.command.JobCommand;
import com.ben.smartcv.common.contract.event.JobEvent;
import com.ben.smartcv.common.job.ExtractedJobData;
import com.ben.smartcv.common.job.JobCreatedEvent;
import com.ben.smartcv.common.util.AuthenticationHelper;
import com.ben.smartcv.common.util.Constant;
import com.ben.smartcv.job.domain.aggregate.JobAggregate;
import com.ben.smartcv.job.domain.model.MasterJob;
import com.ben.smartcv.job.infrastructure.debezium.JobCdcMessage;
import com.ben.smartcv.job.infrastructure.grpc.GrpcClientJobProcessor;
import com.ben.smartcv.job.infrastructure.repository.IMasterJobRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateJobIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GrpcClientJobProcessor grpcClientJobProcessor;

    @Autowired
    private IMasterJobRepository masterJobRepository;

    private FixtureConfiguration<JobAggregate> fixture;

    @BeforeEach
    void setupMockGrpc() {
        ExtractedJobData mockExtract = ExtractedJobData.newBuilder()
                .setEmail(Constant.USER_TEST_EMAIL)
                .setPhone(Constant.USER_TEST_PHONE)
                .addAllEducations(List.of())
                .addAllExperiences(List.of())
                .addAllSkills(List.of())
                .build();

        Mockito.when(grpcClientJobProcessor.callExtractData(Mockito.any()))
                .thenReturn(mockExtract);
    }

    @Test
    void fullCreateJobFlow_success() throws Exception {

        String userId = UUID.randomUUID().toString();
        String jsonRequest = """
            {
                "organizationName": "Test Org",
                "position": "Test position",
                "expiredAt": "2025-06-01T00:00:00.000+00:00",
                "fromSalary": 999,
                "toSalary": 9999,
                "requirements": "Strong ML skills"
            }
        """;

        MvcResult result = mockMvc.perform(request(HttpMethod.POST, "/command")
                        .header("Authorization",
                                "Bearer " + AuthenticationHelper.generateToken(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // Chờ xử lý async (Axon Saga + gRPC + save)
        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    MasterJob latestJob = masterJobRepository.findFirstByOrderByCreatedAtDesc().orElse(null);
                    assertThat(latestJob).isNotEqualTo(null);
                    assert latestJob != null;
                    assertThat(latestJob.getOrganizationName()).isEqualTo("Test Org");
                });

    }

    @Test
    void shouldSendKafkaEventAfterJobCreated() throws Exception {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("", "Constant.KAFKA_GROUP_JOB_CDC", "true");
        KafkaConsumer<String, JobCdcMessage> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList("dbjob.public.job"));

        ConsumerRecords<String, JobCdcMessage> records = consumer.poll(Duration.ofSeconds(5));
        assertThat(records.count()).isGreaterThan(0);

        ConsumerRecord<String, JobCdcMessage> record = records.iterator().next();
        assertThat(record.value().getAfter().getOrganizationName()).isEqualTo("Test Org");
    }

//    @Test
//    void shouldCreateJobJobCreatedEventOnCreateJobCommand() {
//        String id = UUID.randomUUID().toString();
//
//        fixture.givenNoPriorActivity()
//                .when(JobCommand.CreateJob.builder()
//                        .id(id)
//                        .organizationName()
//                        .position()
//                        .fromSalary()
//                        .toSalary()
//                        .requirements()
//                        .expiredAt()
//                        .createdBy()
//                        .build())
//                .expectEvents(JobEvent.JobCreated.builder()
//                        .id(id)
//                        .organizationName()
//                        .position()
//                        .fromSalary()
//                        .toSalary()
//                        .requirements()
//                        .expiredAt()
//                        .createdBy()
//                        .build());
//    }

}

