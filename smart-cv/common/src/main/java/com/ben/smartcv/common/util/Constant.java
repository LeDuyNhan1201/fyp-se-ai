package com.ben.smartcv.common.util;

import com.nimbusds.jose.JWSAlgorithm;
import io.grpc.Context;
import io.grpc.Metadata;

public final class Constant {

    public static final String DEFAULT_PAGE_SIZE = "10";

    public static final String DEFAULT_PAGE_NUMBER = "0";

    public static final String KAFKA_TOPIC_USER_EVENT = "user.events";

    public static final String KAFKA_TOPIC_CV_EVENT = "cv.events";

    public static final String KAFKA_TOPIC_JOB_EVENT = "job.events";

    public static final String KAFKA_TOPIC_NOTIFICATION_EVENT = "notification.events";

    public static final String KAFKA_GROUP_ORCHESTRATION = "orchestration-service";

    public static final String KAFKA_GROUP_JOB_CDC = "job-service-cdc";

    public static final String JOB_CDC_LISTENER_CONTAINER_FACTORY = "jobCdcListenerContainerFactory";

    public static final Metadata.Key<String> AUTHORIZATION_KEY = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<String> GRPC_AUTHORIZATION_CONTEXT = Context.key("BearerToken");

    public static final ThreadLocal<String> REST_AUTHORIZATION_CONTEXT = new ThreadLocal<>();

    public static final JWSAlgorithm JWT_SIGNATURE_ALGORITHM = JWSAlgorithm.HS256;

    public static final String JWT_ISSUER = "com.ben.smart-cv";

    public static final String USER_TEST_EMAIL = "test@gmail.com";

    public static final String USER_TEST_PHONE = "123456789";

}
