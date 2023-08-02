package com.hayba.domain.event;

public interface DomainEvent<T> {
    void fire();
}
