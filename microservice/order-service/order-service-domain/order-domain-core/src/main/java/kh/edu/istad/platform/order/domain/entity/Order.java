package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.*;
import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final BusinessId businessId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private final List<String> failureMessages;
    //if big we doing on version
    private Long version;



    public void pay() {
        if (orderStatus != OrderStatus.PAID){
            throw new OrderDomainException("Order state is PAID");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        requireStatus(OrderStatus.PAID, "approve");
        if (orderStatus != OrderStatus.APPROVED){
            throw new OrderDomainException("Order approve is docuplicate");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initializeOrder() {
        validateOrder();
        setId(new OrderId(UUID.randomUUID()));
        trackingId = TrackingId.newId();
        orderStatus = OrderStatus.PENDING;
        items.forEach(item -> item.initialize(getId(), new OrderItemId(UUID.randomUUID())));
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateInitialOrder() {
        if (getId() != null || orderStatus != null) {
            throw new OrderDomainException("Order is not in correct state for initialization");
        }
    }

    private void validateTotalPrice() {
        if (price ==null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero");
        }
    }

    private void validateItemsPrice() {
        if (items.isEmpty()) {
            throw new OrderDomainException("Order must contain at least one item");
        }
        Money itemsTotal = Money.ZERO;
        for (OrderItem item : items) {
            item.validate();
            itemsTotal = itemsTotal.add(item.getSubTotal());
        }
        if (!price.equals(itemsTotal)) {
            throw new OrderDomainException("Total price " + price.amount()
                    + " is not equal to the sum of order items " + itemsTotal.amount());
        }
    }
    public void initCancel(List<String> messages) {
        requireStatus(OrderStatus.PAID, "initCancel");
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(messages);
    }

    public void cancel(List<String> messages) {
        if (!(orderStatus == OrderStatus.PENDING || orderStatus == OrderStatus.PAID || orderStatus == OrderStatus.CANCELLING)){
            throw new OrderDomainException("Order in this state is can't can't cancell");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(messages);
    }

    private void requireStatus(OrderStatus expected, String operation) {
        if (orderStatus != expected) {
            throw new OrderDomainException(
                    "Order is not in correct for " + operation + " operation");
        }
    }

    private void updateFailureMessages(List<String> messages) {
        if (messages == null) {
            return;
        }
        messages.stream()
                .filter(message -> message != null && !message.isBlank())
                .forEach(failureMessages::add);
    }

    private static <T> T require(T value, String field) {
        if (value == null) {
            throw new OrderDomainException("Order " + field + " is required");
        }
        return value;
    }

    private Order(Builder builder) {
        failureMessages = builder.failureMessages;
        orderStatus = builder.orderStatus;
        trackingId = builder.trackingId;
        items = builder.items;
        price = builder.price;
        deliveryAddress = builder.deliveryAddress;
        businessId = builder.businessId;
        customerId = builder.customerId;
        setId(builder.orderId);
    }


    public CustomerId getCustomerId() {
        return customerId;
    }

    public BusinessId getBusinessId() {
        return businessId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private List<String> failureMessages;
        private OrderStatus orderStatus;
        private TrackingId trackingId;
        private List<OrderItem> items;
        private Money price;
        private StreetAddress deliveryAddress;
        private BusinessId businessId;
        private CustomerId customerId;
        private OrderId orderId;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder businessId(BusinessId val) {
            businessId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder id(OrderId val) {
            orderId = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
