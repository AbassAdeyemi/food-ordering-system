package com.hayba.domain.event.publisher;

import com.hayba.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {
    void publish(T domainEvent);
}
