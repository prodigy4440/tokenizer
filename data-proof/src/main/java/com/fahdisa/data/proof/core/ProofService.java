package com.fahdisa.data.proof.core;

import com.fahdisa.data.common.api.EncryptedInfo;
import com.fahdisa.data.common.config.KafkaTopic;
import com.fahdisa.data.common.core.KeyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProofService {

    private static final Logger log = LoggerFactory.getLogger(ProofService.class);
    private static final ObjectMapper OBJECT_MAPPER = Jackson.newObjectMapper();

    private Consumer<String, String> consumer;
    private ScheduledExecutorService executorService;

    private KeyService keyService;
    private StatefulRedisConnection<String, String> redisConnection;

    public ProofService(Consumer<String, String> consumer, KafkaTopic kafkaTopic,
                        KeyService keyService, StatefulRedisConnection<String, String> redisConnection,
                        ScheduledExecutorService scheduledExecutorService) {
        this.consumer = consumer;
        this.keyService = keyService;
        this.redisConnection = redisConnection;
        this.consumer.subscribe(Arrays.asList(kafkaTopic.getConsumer()));
        this.executorService = scheduledExecutorService;
        this.executorService.scheduleAtFixedRate(() -> {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
            Iterator<ConsumerRecord<String, String>> iterator = consumerRecords.iterator();
            while (iterator.hasNext()) {
                ConsumerRecord<String, String> consumerRecord = iterator.next();
                try {
                    String value = consumerRecord.value();
                    EncryptedInfo encryptedInfo = OBJECT_MAPPER.readValue(value, EncryptedInfo.class);
                    String transactionId = encryptedInfo.getTransactionId();
                    String token = encryptedInfo.getToken();
                    decrypt(transactionId, token);

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }, 2, 2, TimeUnit.SECONDS);
    }

    public void decrypt(String transactionId, String token) {
        String rawKey = getKey(transactionId);
        String decrypt = keyService.decrypt(rawKey, token);
        log.info("Decrypted Data for Transaction ID [{}] is [{}]", transactionId, decrypt);
    }


    private String getKey(String transactionId) {
        return redisConnection.sync().get(toDbKey(transactionId));
    }

    private String toDbKey(String transactionId) {
        return String.format("%s:%s", "data-flow-key", transactionId);
    }
}
