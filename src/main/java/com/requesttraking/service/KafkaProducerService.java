package com.requesttraking.service;

import com.requesttraking.dto.PersonForKafka;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaProducerService {
    private KafkaTemplate<String, PersonForKafka> kafkaTemplate;

    public void sendMessage(PersonForKafka message) {
        kafkaTemplate.send("request-topic", message);
        log.warn(String.format("Сообщение отправлено: [%s]", message));
    }
}
