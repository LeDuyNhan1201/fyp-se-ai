package com.ben.smartcv.common.util;

import org.slf4j.Logger;
import java.time.Instant;

public final class LogHelper {

    public static void logMessage(Logger log, String messageType, String correlationId, String causationId, Object payload) {
        log.debug("[{}]: Message: {} | correlationId: {} | causationId: {} | payload: {}",
                log.getName(), messageType, correlationId, causationId, payload);
    }

    public static void logError(Logger log, String reason, Exception exception) {
        log.error("[{}]: Reason: {} | class: {} | line: {}",
                log.getName(), reason, exception.getClass(), exception.getStackTrace()[0].getLineNumber());
    }

}
