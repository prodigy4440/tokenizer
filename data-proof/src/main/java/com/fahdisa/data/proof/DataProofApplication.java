package com.fahdisa.data.proof;

import com.fahdisa.data.common.config.KafkaTopic;
import com.fahdisa.data.common.core.KeyService;
import com.fahdisa.data.proof.core.ProofService;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.kafka.KafkaConsumerBundle;
import io.dropwizard.kafka.KafkaConsumerFactory;
import io.dropwizard.redis.RedisClientBundle;
import io.dropwizard.redis.RedisClientFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

public class DataProofApplication extends Application<DataProofConfiguration> {

    private final RedisClientBundle<String, String, DataProofConfiguration> redis =
            new RedisClientBundle<String, String, DataProofConfiguration>() {
                @Override
                public RedisClientFactory<String, String> getRedisClientFactory(DataProofConfiguration configuration) {
                    return configuration.getRedisClientFactory();
                }
            };

    private final KafkaConsumerBundle<String, String, DataProofConfiguration> kafkaConsumer =
            new KafkaConsumerBundle<String, String, DataProofConfiguration>(
                    Collections.emptyList(), new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                }
            }) {
                @Override
                public KafkaConsumerFactory<String, String> getKafkaConsumerFactory(DataProofConfiguration configuration) {
                    return configuration.getKafkaConsumerFactory();
                }
            };

    public static void main(final String[] args) throws Exception {
        new DataProofApplication().run(args);
    }

    @Override
    public String getName() {
        return "data-proof";
    }

    @Override
    public void initialize(final Bootstrap<DataProofConfiguration> bootstrap) {
        bootstrap.addBundle(redis);

        bootstrap.addBundle(kafkaConsumer);

        bootstrap.addBundle(new SwaggerBundle<DataProofConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DataProofConfiguration dataSourceConfiguration) {
                return dataSourceConfiguration.getSwaggerBundleConfiguration();
            }
        });
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                ));
    }

    @Override
    public void run(final DataProofConfiguration configuration, final Environment environment) {

        StatefulRedisConnection<String, String> redisConnection = redis.getConnection();

        KafkaTopic kafkaTopic = configuration.getKafkaTopic();

        Consumer<String, String> consumer = kafkaConsumer.getConsumer();

        ScheduledExecutorService scheduledExecutorService = environment.lifecycle()
                .scheduledExecutorService("data-proof").build();


        KeyService keyService = new KeyService();

        ProofService proofService = new ProofService(consumer, kafkaTopic, keyService, redisConnection, scheduledExecutorService);
    }

}
