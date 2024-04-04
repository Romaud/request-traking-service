package com.requesttraking.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableKafka
public class KafkaConsumerService {

    @KafkaListener(
            groupId = "group-avro",
            topics = "avro-topic",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumer(ConsumerRecord<String, GenericRecord> message) {
        log.warn(String.format("Сообщение получено в Avro формате: [%s]", message));
    }
}