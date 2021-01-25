package com.fahdisa.data.source;

import com.fahdisa.data.common.config.KafkaTopic;
import com.fahdisa.data.source.core.CardService;
import com.fahdisa.data.source.resources.CardResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.kafka.KafkaProducerBundle;
import io.dropwizard.kafka.KafkaProducerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.apache.kafka.clients.producer.Producer;

import java.util.Collections;

public class DataSourceApplication extends Application<DataSourceConfiguration> {

    private final KafkaProducerBundle<String, String, DataSourceConfiguration> kafkaProducer =
            new KafkaProducerBundle<String, String, DataSourceConfiguration>(
                    Collections.emptyList()) {
                @Override
                public KafkaProducerFactory<String, String> getKafkaProducerFactory(DataSourceConfiguration configuration) {
                    return configuration.getKafkaProducerFactory();
                }
            };


    public static void main(final String[] args) throws Exception {
        new DataSourceApplication().run(args);
    }

    @Override
    public String getName() {
        return "data-source";
    }

    @Override
    public void initialize(final Bootstrap<DataSourceConfiguration> bootstrap) {

        bootstrap.addBundle(kafkaProducer);

        bootstrap.addBundle(new SwaggerBundle<DataSourceConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DataSourceConfiguration dataSourceConfiguration) {
                return dataSourceConfiguration.getSwaggerBundleConfiguration();
            }
        });
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
    }

    @Override
    public void run(final DataSourceConfiguration configuration, final Environment environment) {
        KafkaTopic kafkaTopic = configuration.getKafkaTopic();
        Producer<String, String> producer = kafkaProducer.getProducer();
        CardService cardService = new CardService(producer, kafkaTopic);
        CardResource cardResource = new CardResource(cardService);
        environment.jersey().register(cardResource);
    }

}
