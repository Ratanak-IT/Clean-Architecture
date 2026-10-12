package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    public Customer(CustomerId customerId) {
        setId(customerId);
    }
}
