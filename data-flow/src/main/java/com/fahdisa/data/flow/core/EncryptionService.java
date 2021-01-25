package com.fahdisa.data.flow.core;

import com.fahdisa.data.common.api.EncryptedInfo;
import com.fahdisa.data.common.config.KafkaTopic;
import com.fahdisa.data.common.core.KeyService;
import com.fahdisa.data.flow.db.CardModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EncryptionService {

    private static final Logger log = LoggerFactory.getLogger(EncryptionService.class);
    private static final ObjectMapper OBJECT_MAPPER = Jackson.newObjectMapper();

    private Producer<String, String> producer;
    private Consumer<String, String> consumer;
    private KafkaTopic kafkaTopic;
    private ScheduledExecutorService executorService;
    private KeyService keyService;
    private StatefulRedisConnection<String, String> redisConnection;

    public EncryptionService(Producer<String, String> producer,
                             Consumer<String, String> consumer,
                             KafkaTopic kafkaTopic,
                             KeyService keyService,
                             StatefulRedisConnection<String, String> redisConnection,
                             ScheduledExecutorService scheduledExecutorService) {
        this.producer = producer;
        this.consumer = consumer;
        this.kafkaTopic = kafkaTopic;
        this.keyService = keyService;
        this.redisConnection = redisConnection;
        this.executorService = scheduledExecutorService;
        this.consumer.subscribe(Arrays.asList(kafkaTopic.getConsumer()));
        this.executorService.scheduleAtFixedRate(() -> {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
            Iterator<ConsumerRecord<String, String>> iterator = consumerRecords.iterator();
            while (iterator.hasNext()) {
                ConsumerRecord<String, String> consumerRecord = iterator.next();
                try {
                    String key = consumerRecord.key();
                    String value = consumerRecord.value();
                    CardModel cardModel = OBJECT_MAPPER.readValue(value, CardModel.class);
                    encrypt(cardModel);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }, 2, 2, TimeUnit.SECONDS);
    }

    //Sink
    public void encrypt(CardModel cardModel) {
        cardModel.getPan();
        cardModel.getExpiration();
        cardModel.getCvv();
        Map<String, String> map = keyService.encrypt(cardModel.getTransactionId(), cardModel.getPan(),
                cardModel.getExpiration(), cardModel.getCvv());
        if (Objects.nonNull(map)) {
            try {
                String key = map.get("key");
                String token = map.get("token");
                saveKey(cardModel.getTransactionId(), key);
                EncryptedInfo encryptedInfo = new EncryptedInfo(token, cardModel.getTransactionId());
                String json = OBJECT_MAPPER.writeValueAsString(encryptedInfo);
                final ProducerRecord<String, String> record = new ProducerRecord<>(kafkaTopic.getProducer(), cardModel.getTransactionId(), json);
                final Future<RecordMetadata> metadataFuture = producer.send(record);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveKey(String transactionId, String encryptionKey) {
        redisConnection.sync().set(toDbKey(transactionId), encryptionKey);
    }

    public String toDbKey(String transactionId) {
        return String.format("%s:%s", "data-flow-key", transactionId);
    }

}
