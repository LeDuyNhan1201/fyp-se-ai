package com.ben.smartcv.common.util;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

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

    public static Instant generateRandomInstant() {
        // Get the current time
        Instant now = Instant.now();

        // Generate a random number of days between 2 and 3 months
        long daysInTwoMonths = 60; // Approximate number of days in 2 months
        long daysInThreeMonths = 90; // Approximate number of days in 3 months
        long randomDays = ThreadLocalRandom.current().nextLong(daysInTwoMonths, daysInThreeMonths);

        // Add the random number of days to the current Instant
        return now.plus(Duration.ofDays(randomDays));
    }

}
