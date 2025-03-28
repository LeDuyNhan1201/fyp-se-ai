package com.ben.smartcv.common.util;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class TimeHelper {

    public static Timestamp convertToTimestamp(Instant instant) {
        return Timestamps.fromMillis(instant.toEpochMilli());
    }

    public static Instant convertToInstant(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

    public static OffsetDateTime convertToOffsetDateTime(Instant instant, ZoneOffset zoneOffset) {
        return OffsetDateTime.ofInstant(instant, zoneOffset);
    }

}
