package com.dio.account.query.infrastructure.consumers;

import com.dio.account.common.events.AccountClosedEvent;
import com.dio.account.common.events.AccountOpenedEvent;
import com.dio.account.common.events.FundsDepositedEvent;
import com.dio.account.common.events.FundsWithdrawnEvent;
import com.dio.account.query.infrastructure.handlers.EventHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class AccountEventConsumer implements EventConsumer{
    private final EventHandler handler;

    public AccountEventConsumer(EventHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "AccountOpenedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountOpenedEvent event, Acknowledgment ack) {
        handler.on(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "FundsDepositedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsDepositedEvent event, Acknowledgment ack) {
        handler.on(event);
        ack.acknowledge();
    }
    @KafkaListener(topics = "FundsWithdrawnEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsWithdrawnEvent event, Acknowledgment ack) {
        handler.on(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "AccountClosedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountClosedEvent event, Acknowledgment ack) {
        handler.on(event);
        ack.acknowledge();
    }
}
