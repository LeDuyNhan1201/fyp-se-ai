package com.ben.smartcv.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class EventLogger {
    private static final Logger logger = LoggerFactory.getLogger(EventLogger.class);

    public static void logEvent(String eventName, String correlationId, String causationId, Map<String, Object> payload) {
        String logMessage = String.format(
            "{ \"event\": \"%s\", \"timestamp\": \"%s\", \"correlationId\": \"%s\", \"causationId\": \"%s\", \"payload\": %s }",
            eventName, Instant.now(), correlationId, causationId, payload
        );

        logger.info(logMessage);
    }

    public static void logCommand(String commandName, String correlationId, Map<String, Object> payload) {
        logEvent("Command:" + commandName, correlationId, null, payload);
    }

    public static void logError(String eventName, String correlationId, String causationId, String errorMessage) {
        logEvent(eventName + ".Failed", correlationId, causationId, Map.of("error", errorMessage));
    }

    public static void main(String[] args) {
        String correlationId = UUID.randomUUID().toString();
        String causationId = UUID.randomUUID().toString();

        logCommand("RegisterUser", correlationId, Map.of("userId", "123", "email", "test@example.com"));
        logEvent("UserRegistered", correlationId, causationId, Map.of("userId", "123"));
        logError("UserRegistered", correlationId, causationId, "Email already exists");
    }
}
