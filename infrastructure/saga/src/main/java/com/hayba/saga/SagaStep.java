package com.hayba.saga;

import com.hayba.domain.event.DomainEvent;

public interface SagaStep<T> {
    void process(T data);
    void rollback(T data);
}
