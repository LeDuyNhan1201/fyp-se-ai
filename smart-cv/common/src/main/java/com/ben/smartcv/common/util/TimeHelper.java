package com.ben.smartcv.common.util;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;

import java.time.Instant;

public class TimeHelper {

    public static Timestamp convertToTimestamp(Instant instant) {
        return Timestamps.fromMillis(instant.toEpochMilli());
    }

    public static Instant convertToInstant(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

}
