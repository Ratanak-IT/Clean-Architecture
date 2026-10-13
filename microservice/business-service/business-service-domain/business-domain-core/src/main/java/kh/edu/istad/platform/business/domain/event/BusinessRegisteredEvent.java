package kh.edu.istad.platform.business.domain.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.business.domain.entity.Business;

import java.time.ZonedDateTime;

public class BusinessRegisteredEvent implements DomainEvent<Business> {
    private final Business business;
    private final ZonedDateTime createdAt;

    public BusinessRegisteredEvent(Business business, ZonedDateTime createdAt) {
        this.business = business;
        this.createdAt = createdAt;
    }

    public Business getBusiness() {
        return business;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
