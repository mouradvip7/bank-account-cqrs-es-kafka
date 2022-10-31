package com.dio.account.cmd.infrastructure;

import com.dio.account.cmd.domain.AccountAggregate;
import com.dio.account.cmd.domain.repository.EventStoreRepository;
import com.dio.cqrs.core.events.BaseEvent;
import com.dio.cqrs.core.events.EventModel;
import com.dio.cqrs.core.exceptions.AggregateNotFoundException;
import com.dio.cqrs.core.exceptions.ConcurrencyException;
import com.dio.cqrs.core.infrastructure.EventStore;
import com.dio.cqrs.core.producers.EventProducer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {

    private final EventStoreRepository repository;

    private final EventProducer producer;

    AccountEventStore(EventStoreRepository repository, EventProducer producer){
        this.repository = repository;
        this.producer = producer;
    }

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStream = repository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion)
            throw new ConcurrencyException();
        var version = expectedVersion;
        for (var event: events){
            version++;
            event.setVersion(version);
            var eventModel = EventModel.builder()
                    .timeStamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();
            var persistedEvent = repository.save(eventModel);
            if(!persistedEvent.getId().isEmpty()){
                producer.produce(event.getClass().getSimpleName(),event);
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventStream = repository.findByAggregateIdentifier(aggregateId);
        if (eventStream == null || eventStream.isEmpty())
            throw new AggregateNotFoundException("Incorrect account ID provided");
        return eventStream.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }

    @Override
    public List<String> getAggregateIds() {
        var eventStream = repository.findAll();
        if(eventStream == null || eventStream.isEmpty() ){
            throw new IllegalStateException("Could not retrieve event stream from the event store!");
        }
        return eventStream.stream().map(EventModel::getAggregateIdentifier).distinct().collect(Collectors.toList());
    }
}
