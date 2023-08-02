package com.hayba.payment.service.domain.ports.output.repository;

import com.hayba.domain.valueobject.CustomerId;
import com.hayba.payment.service.domain.entity.CreditEntry;

import java.util.Optional;

public interface CreditEntryRepository {

    CreditEntry save(CreditEntry creditEntry);

    Optional<CreditEntry> findByCustomerId(CustomerId customerId);
}
