package com.fahdisa.data.proof;

import com.fahdisa.data.common.config.KafkaTopic;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.kafka.KafkaConsumerFactory;
import io.dropwizard.kafka.KafkaProducerFactory;
import io.dropwizard.redis.RedisClientFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class DataProofConfiguration extends Configuration {

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @Valid
    @NotNull
    @JsonProperty("redis")
    private RedisClientFactory<String, String> redisClientFactory;

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
