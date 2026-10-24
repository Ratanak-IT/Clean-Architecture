package kh.edu.istad.platform.customer.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.exception.CustomerException;

import java.util.UUID;
public class CustomerSegment extends AggregateRoot<CustomerSegmentId> {
    private String code;
    private final String name;

    public void updateCode(String code) {
        if (code == null || code.isBlank()) {
            throw new CustomerException("Customer segment code must not be blank");
        }
        this.code = code;
    }

    public void initiateCustomerSegment() {
        validateInitiate();
        super.setId(new CustomerSegmentId(UUID.randomUUID()));
    }

    private void validateInitiate() {
        if (getId() != null) {
            throw new CustomerException("Customer segment is already initialized");
        }
        if (code == null || code.isBlank()) {
            throw new CustomerException("Customer segment code must not be blank");
        }
    }

    public String getCode() { return code; }
    public String getName() { return name; }

    private CustomerSegment(Builder builder) {
        super.setId(builder.id);
        code = builder.code;
        name = builder.name;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private CustomerSegmentId id;
        private String code;
        private String name;

        private Builder() {}

        public Builder id(CustomerSegmentId val) { id = val; return this; }
        public Builder code(String val) { code = val; return this; }
        public Builder name(String val) { name = val; return this; }
        public CustomerSegment build() { return new CustomerSegment(this); }
    }
}