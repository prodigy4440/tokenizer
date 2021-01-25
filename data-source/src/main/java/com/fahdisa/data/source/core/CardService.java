package com.fahdisa.data.source.core;

import com.fahdisa.data.common.api.CardInfo;
import com.fahdisa.data.common.config.KafkaTopic;
import com.fahdisa.data.common.core.IdGenerator;
import com.fahdisa.data.source.api.ApiResponse;
import com.fahdisa.data.source.db.CardModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);
    private static final ObjectMapper OBJECT_MAPPER = Jackson.newObjectMapper();

    private Producer<String, String> producer;
    private KafkaTopic kafkaTopic;

    public CardService(Producer<String, String> producer, KafkaTopic kafkaTopic) {
        this.producer = producer;
        this.kafkaTopic = kafkaTopic;
    }

    public ApiResponse submit(CardInfo cardInfo) {

        try {
            String transactionId = IdGenerator.nextId();
            CardModel cardModel = new CardModel(transactionId, cardInfo.getCardNumber(),
                    cardInfo.getExpirationDate(), cardInfo.getCvv());
            String json = OBJECT_MAPPER.writeValueAsString(cardModel);

            final ProducerRecord<String, String> record = new ProducerRecord<>(kafkaTopic.getProducer(), transactionId, json);
            final Future<RecordMetadata> metadataFuture = producer.send(record);

            // wait for message to be successfully produced
            final RecordMetadata metadata = metadataFuture.get();
            System.out.println(metadata);
            log.info("Meta Info [{}]", metadata);
            log.info("CardInfo pushed to kafka ID: [{}], CardPan [{}]", transactionId, cardModel.getPan());
            return new ApiResponse(true, transactionId);
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            log.error("Error processing [{}], Message [{}]", cardInfo.getCardNumber(), e.getCause().getMessage());
        }
        return new ApiResponse(false, "");
    }
}
