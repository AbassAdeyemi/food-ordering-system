package com.hayba.outbox;

public interface OutboxScheduler {
    void processOutboxMessage();
}
