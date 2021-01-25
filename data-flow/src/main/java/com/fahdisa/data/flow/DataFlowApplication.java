package com.fahdisa.data.flow;

import com.fahdisa.data.common.config.KafkaTopic;
import com.fahdisa.data.common.core.KeyService;
import com.fahdisa.data.flow.core.EncryptionService;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.kafka.KafkaConsumerBundle;
import io.dropwizard.kafka.KafkaConsumerFactory;
import io.dropwizard.kafka.KafkaProducerBundle;
import io.dropwizard.kafka.KafkaProducerFactory;
import io.dropwizard.redis.RedisClientBundle;
import io.dropwizard.redis.RedisClientFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

public class DataFlowApplication extends Application<DataFlowConfiguration> {


    private final RedisClientBundle<String, String, DataFlowConfiguration> redis =
            new RedisClientBundle<String, String, DataFlowConfiguration>() {
                @Override
                public RedisClientFactory<String, String> getRedisClientFactory(DataFlowConfiguration configuration) {
                    return configuration.getRedisClientFactory();
                }
            };

    private final KafkaProducerBundle<String, String, DataFlowConfiguration> kafkaProducer =
            new KafkaProducerBundle<String, String, DataFlowConfiguration>(
                    Collections.emptyList()) {
                @Override
                public KafkaProducerFactory<String, String> getKafkaProducerFactory(DataFlowConfiguration configuration) {
                    return configuration.getKafkaProducerFactory();
                }
            };

    private final KafkaConsumerBundle<String, String, DataFlowConfiguration> kafkaConsumer =
            new KafkaConsumerBundle<String, String, DataFlowConfiguration>(
                    Collections.emptyList(), new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                }}) {
                @Override
                public KafkaConsumerFactory<String, String> getKafkaConsumerFactory(DataFlowConfiguration configuration) {
                    return configuration.getKafkaConsumerFactory();
                }
            };


    public static void main(final String[] args) throws Exception {
        new DataFlowApplication().run(args);
    }

    @Override
    public String getName() {
        return "data-flow";
    }

    @Override
    public void initialize(final Bootstrap<DataFlowConfiguration> bootstrap) {

        bootstrap.addBundle(redis);

        bootstrap.addBundle(kafkaProducer);

        bootstrap.addBundle(kafkaConsumer);

        bootstrap.addBundle(new SwaggerBundle<DataFlowConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DataFlowConfiguration dataSourceConfiguration) {
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
    public void run(final DataFlowConfiguration configuration, final Environment environment) {

        StatefulRedisConnection<String, String> redisConnection = redis.getConnection();

        KafkaTopic kafkaTopic = configuration.getKafkaTopic();

        Producer<String, String> producer = kafkaProducer.getProducer();

        Consumer<String, String> consumer = kafkaConsumer.getConsumer();

        ScheduledExecutorService scheduledExecutorService = environment.lifecycle()
                .scheduledExecutorService("data-flow").build();

        KeyService keyService = new KeyService();

        EncryptionService encryptionService = new EncryptionService(producer, consumer, kafkaTopic,
                keyService, redisConnection,  scheduledExecutorService);

    }

}
