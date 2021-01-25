package com.fahdisa.data.source;

import com.fahdisa.data.common.config.KafkaTopic;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.kafka.KafkaProducerFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DataSourceConfiguration extends Configuration {

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @Valid
    @NotNull
    @JsonProperty("producer")
    private KafkaProducerFactory<String, String> kafkaProducerFactory;

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

    public KafkaProducerFactory<String, String> getKafkaProducerFactory() {
        return kafkaProducerFactory;
    }

    public void setKafkaProducerFactory(KafkaProducerFactory<String, String> kafkaProducerFactory) {
        this.kafkaProducerFactory = kafkaProducerFactory;
    }

    public KafkaTopic getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(KafkaTopic kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

}
