package com.hayba.payment.service.domain;

import com.hayba.payment.service.domain.entity.CreditEntry;
import com.hayba.payment.service.domain.entity.CreditHistory;
import com.hayba.payment.service.domain.entity.Payment;
import com.hayba.payment.service.domain.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvent validateAndInitiatePayment(Payment payment,
                                            CreditEntry creditEntry,
                                            List<CreditHistory> creditHistories,
                                            List<String> failureMessages);

    PaymentEvent validateAndCancelPayment(Payment payment,
                                          CreditEntry creditEntry,
                                          List<CreditHistory> creditHistories,
                                          List<String> failureMessages);
}
