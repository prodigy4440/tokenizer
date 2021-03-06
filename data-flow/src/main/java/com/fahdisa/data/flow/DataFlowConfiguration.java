package com.fahdisa.data.flow;

import com.fahdisa.data.common.config.KafkaTopic;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.kafka.KafkaConsumerFactory;
import io.dropwizard.kafka.KafkaProducerFactory;
import io.dropwizard.redis.RedisClientFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DataFlowConfiguration extends Configuration {

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @Valid
    @NotNull
    @JsonProperty("redis")
    private RedisClientFactory<String, String> redisClientFactory;

    @Valid
    @NotNull
    @JsonProperty("producer")
    private KafkaProducerFactory<String, String> kafkaProducerFactory;

    @Valid
    @NotNull
    @JsonProperty("consumer")
    private KafkaConsumerFactory<String, String> kafkaConsumerFactory;

    @Valid
    @NotNull
    @JsonProperty("topics")
    private KafkaTopic kafkaTopic;

    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }

    public void setSwaggerBundleConfiguration(SwaggerBundleConfiguration swaggerBundleConfiguration) {
        this.swaggerBundleConfiguration = swaggerBundleConfiguration;
    }

    public RedisClientFactory<String, String> getRedisClientFactory() {
        return redisClientFactory;
    }

    public void setRedisClientFactory(RedisClientFactory<String, String> redisClientFactory) {
        this.redisClientFactory = redisClientFactory;
    }

    public KafkaProducerFactory<String, String> getKafkaProducerFactory() {
        return kafkaProducerFactory;
    }

    public void setKafkaProducerFactory(KafkaProducerFactory<String, String> kafkaProducerFactory) {
        this.kafkaProducerFactory = kafkaProducerFactory;
    }

    public KafkaConsumerFactory<String, String> getKafkaConsumerFactory() {
        return kafkaConsumerFactory;
    }

    public void setKafkaConsumerFactory(KafkaConsumerFactory<String, String> kafkaConsumerFactory) {
        this.kafkaConsumerFactory = kafkaConsumerFactory;
    }

    public KafkaTopic getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(KafkaTopic kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

}
