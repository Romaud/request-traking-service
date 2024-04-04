package com.requesttraking.config;

import com.requesttraking.dto.PersonForKafka;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaProducerConfig {
    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaTemplate<String, PersonForKafka> kafkaTemplate() {
        var props = kafkaProperties.buildConsumerProperties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://127.0.0.1:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        var producerFactory = new DefaultKafkaProducerFactory<String, PersonForKafka>(props);

        return new KafkaTemplate<>(producerFactory);
    }
}
