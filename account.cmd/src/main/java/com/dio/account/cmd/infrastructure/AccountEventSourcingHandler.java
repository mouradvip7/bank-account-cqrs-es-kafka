package com.dio.account.cmd.infrastructure;

import com.dio.account.cmd.domain.AccountAggregate;
import com.dio.cqrs.core.domain.AggregateRoot;
import com.dio.cqrs.core.events.BaseEvent;
import com.dio.cqrs.core.handlers.EventSourcingHandler;
import com.dio.cqrs.core.infrastructure.EventStore;
import com.dio.cqrs.core.producers.EventProducer;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
    private final EventStore eventStore;

    private final EventProducer producer;

    public AccountEventSourcingHandler(EventStore eventStore, EventProducer producer){
        this.eventStore = eventStore;
        this.producer = producer;
    }

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate = new AccountAggregate();
        var events = eventStore.getEvents(id);
        if (events != null && !events.isEmpty()){
            aggregate.replayEvents(events);
            var latestVersion = events.stream().map(BaseEvent::getVersion).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }
        return aggregate;
    }

    @Override
    public void republishEvents() {
        var aggregateIds = eventStore.getAggregateIds();
        for (var aggregateId:aggregateIds){
            var aggregate = getById(aggregateId);
            if (aggregate ==null || !aggregate.getActive()) continue;
            var events = eventStore.getEvents(aggregateId);
            for (var event: events){
                producer.produce(event.getClass().getSimpleName(), event);
            }
        }
    }
}
