package kh.edu.istad.platform.domain.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public interface PaymentEvent extends DomainEvent<Payment> {
    Payment getPayment();
    ZonedDateTime getCreatedAt();
    List<String> getFailureMessages();
}
