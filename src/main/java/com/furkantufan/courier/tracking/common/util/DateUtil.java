package com.furkantufan.courier.tracking.common.util;

import lombok.experimental.UtilityClass;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@UtilityClass
public final class DateUtil {

    public static final ZoneId ZONE_UTC = ZoneId.of("UTC");

    private static final Clock CLOCK = Clock.systemUTC();

    public static Instant getCurrentInstant() {
        return Instant.now(CLOCK);
    }

    public static Instant getStartOfDayInstantAtUTC() {
        LocalDate today = LocalDate.now();
        ZonedDateTime startOfDay = today.atStartOfDay(ZONE_UTC);
        return startOfDay.toInstant();
    }
}