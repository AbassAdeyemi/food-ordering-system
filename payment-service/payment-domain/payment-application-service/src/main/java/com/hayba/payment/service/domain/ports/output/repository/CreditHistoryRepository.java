package com.hayba.payment.service.domain.ports.output.repository;

import com.hayba.domain.valueobject.CustomerId;
import com.hayba.payment.service.domain.entity.CreditHistory;

import java.util.List;
import java.util.Optional;

public interface CreditHistoryRepository {

    CreditHistory save(CreditHistory creditHistory);

    Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId);
}
