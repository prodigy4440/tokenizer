package com.fahdisa.data.common.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

    private static Logger log = LoggerFactory.getLogger(IdGenerator.class);

    public static volatile long LAST_TIMESTAMP = -1;
    public static AtomicInteger COUNTER = new AtomicInteger();

    public static String nextId() {
        return nextId(null);
    }

    public static String nextId(Integer machineId) {

        if (Objects.isNull(machineId)) {
            machineId = 1;
        }

        int sequence = 0;
        long timestamp = Instant.now().toEpochMilli();
        if (timestamp < LAST_TIMESTAMP) {
            log.info("Should not allow generating time from the past");
            System.out.println("illegal");
        } else if (timestamp == LAST_TIMESTAMP) {
            sequence = COUNTER.getAndIncrement();
        } else {
            COUNTER.set(0);
            sequence = COUNTER.getAndIncrement();
        }
        LAST_TIMESTAMP = timestamp;

        StringBuilder builder = new StringBuilder();
        return builder.append(timestamp).append(String.format("%03d", machineId)).append(String.format("%04d", sequence)).toString();
    }

}
