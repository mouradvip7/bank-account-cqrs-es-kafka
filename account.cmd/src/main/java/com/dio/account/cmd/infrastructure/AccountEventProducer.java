package com.dio.account.cmd.infrastructure;

import com.dio.cqrs.core.events.BaseEvent;
import com.dio.cqrs.core.producers.EventProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountEventProducer implements EventProducer {

    private final KafkaTemplate<String, Object> template;

    public AccountEventProducer(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    @Override
    public void produce(String topic, BaseEvent event) {
        this.template.send(topic, event);

    }
}
