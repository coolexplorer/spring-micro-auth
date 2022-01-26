package io.coolexplorer.auth.config;

import io.coolexplorer.auth.message.JwtTokenMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaJwtTokenProducerConfig {
    private final String bootStrapAddresses;

    public KafkaJwtTokenProducerConfig(@Value("${kafka.bootstrap.addresses}") String bootStrapAddresses) {
        this.bootStrapAddresses = bootStrapAddresses;
    }

    private Map<String, Object> getJsonProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapAddresses);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Object> jwtTokenProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getJsonProperties());
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaJwtTokenTemplate() {
        return new KafkaTemplate<>(jwtTokenProducerFactory());
    }

}
