package com.ben.smartcv.common.util;

import java.time.Instant;
import java.util.Map;

public class EventLogger {

    public static String logEvent(String eventName, String correlationId, String causationId, Map<String, Object> payload) {
        String logMessage = String.format(
            "{ \"event\": \"%s\", \"timestamp\": \"%s\", \"correlationId\": \"%s\", \"causationId\": \"%s\", \"payload\": %s }",
            eventName, Instant.now(), correlationId, causationId, payload
        );

        return logMessage;
    }

    public static String logCommand(String commandName, String correlationId, Map<String, Object> payload) {
        return logEvent("Command:" + commandName, correlationId, null, payload);
    }

    public static String logError(String eventName, String correlationId, String causationId, String errorMessage) {
        return logEvent(eventName + ".Failed", correlationId, causationId, Map.of("error", errorMessage));
    }

}
