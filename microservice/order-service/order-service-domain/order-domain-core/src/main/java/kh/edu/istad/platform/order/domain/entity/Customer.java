package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
//    private final String username;
//    private final String familyName;
//    private final String givenName;

    public Customer(CustomerId customerId) {
        setId(customerId);
//        this.username=username;
//        this.familyName=familyName;
//        this.givenName=givenName;
    }
}
