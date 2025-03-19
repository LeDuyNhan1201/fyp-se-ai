package com.ben.smartcv.common.util;

import io.grpc.Context;
import io.grpc.Metadata;

public class Constant {

    public static final String KAFKA_TOPIC_USER_EVENT = "user.events";

    public static final String KAFKA_TOPIC_CV_EVENT = "cv.events";

    public static final String KAFKA_TOPIC_CV_COMMAND = "cv.commands";

    public static final String  KAFKA_TOPIC_JOB_COMMAND = "job.commands";

    public static final String KAFKA_TOPIC_JOB_EVENT = "job.events";

    public static final String KAFKA_TOPIC_NOTIFICATION_COMMAND = "notification.commands";

    public static final String KAFKA_TOPIC_NOTIFICATION_EVENT = "notification.events";

    public static final String KAFKA_GROUP_ORCHESTRATION = "orchestration-service";

    public static final String KAFKA_GROUP_NOTIFICATION = "notification-service";

    public static final String KAFKA_GROUP_CV_PROCESSOR = "cv-processor";

    public static final String KAFKA_GROUP_JOB_PROCESSOR = "job-processor";

    public static final Metadata.Key<String> AUTHORIZATION_KEY = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<String> GRPC_AUTHORIZATION_CONTEXT = Context.key("authToken");

    public static final ThreadLocal<String> REST_AUTHORIZATION_CONTEXT = new ThreadLocal<>();

    //public static final JWSAlgorithm ACCESS_TOKEN_SIGNATURE_ALGORITHM = HS512;

}
