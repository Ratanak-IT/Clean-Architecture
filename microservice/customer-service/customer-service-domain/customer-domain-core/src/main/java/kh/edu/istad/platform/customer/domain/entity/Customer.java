package kh.edu.istad.platform.customer.domain.entity;


import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.exception.CustomerException;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;

import java.util.UUID;


public class Customer extends AggregateRoot<CustomerId> {
//    private final CustomerId customerId;
    private final String username;
    private String familyName;
    private String givenName;
    private final Email email;
    private final PhoneNumber phoneNumber;
    private CustomerStatus customerStatus;



    public void initiateCustomer(){
        validateCustomer();
        super.setId(new CustomerId(UUID.randomUUID()));
        customerStatus = CustomerStatus.ACTIVE;
    }
    public void validateCustomer(){
        if(super.getId() !=null){
            throw new CustomerException("Customer id must be null");
        }
        if(customerStatus !=null){
            throw new CustomerException("Customer status be null");
        }
    }

    public void updateCustomer(String familyName, String givenName){
        if(familyName == null || givenName ==  null){
            throw new CustomerException("Familyname and givenName must not be null");
        }
        this.familyName =familyName;
        this.givenName = givenName;
    }

    public void deactivateCustomer(){
        if (customerStatus != CustomerStatus.ACTIVE) {
            throw new CustomerException(" Could not deactive because customer not status");
        }
        customerStatus = CustomerStatus.INACTIVE;
    }



    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Email getEmail() {
        return email;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getUsername() {
        return username;
    }

    private Customer(Builder builder) {
        super.setId(builder.id);
        username = builder.username;
        familyName = builder.familyName;
        givenName = builder.givenName;
        email = builder.email;
        phoneNumber = builder.phoneNumber;
        customerStatus = builder.customerStatus;
    }


    public static final class Builder {
        private CustomerId id;
        private String username;
        private String familyName;
        private String givenName;
        private Email email;
        private PhoneNumber phoneNumber;
        private CustomerStatus customerStatus;




        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(CustomerId val) {
            id = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder familyName(String val) {
            familyName = val;
            return this;
        }

        public Builder givenName(String val) {
            givenName = val;
            return this;
        }

        public Builder email(Email val) {
            email = val;
            return this;
        }

        public Builder phoneNumber(PhoneNumber val) {
            phoneNumber = val;
            return this;
        }

        public Builder customerStatus(CustomerStatus val) {
            customerStatus = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
