package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.OrderItemId;
import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

public class OrderItem extends BaseEntity<OrderItemId> {
    private OrderId orderId;
    private final Product product;
    private final int quantity;
    private final Money price;
    private final Money subTotal;

    void initialize(OrderId orderId, OrderItemId orderItemId) {
        this.orderId = orderId;
        setId(orderItemId);
    }

    public boolean isPriceValid() {
        return price.equals(product.getPrice())
                && price.multiply(quantity).equals(subTotal);
    }


    void validate() {
        if (product == null || product.getId() == null) {
            throw new OrderDomainException("Order item must reference a product");
        }
        if (product.getPrice() == null) {
            throw new OrderDomainException(
                    "Product " + product.getId().value() + " is not confirmed by the business");
        }
        if (quantity < 1) {
            throw new OrderDomainException(
                    "Quantity of product " + product.getId().value() + " must be at least 1");
        }
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException(
                    "Price of product " + product.getId().value() + " must be greater than zero");
        }
        if (!isPriceValid()) {
            throw new OrderDomainException("Order item price " + price.amount()
                    + " is not valid for product " + product.getId().value());
        }
    }

    private OrderItem(Builder builder) {
        setId(builder.orderItemId);
        orderId = builder.orderId;
        product = builder.product;
        quantity = builder.quantity;
        price = builder.price;
        subTotal = builder.subTotal;
    }


    public OrderId getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubTotal() {
        return subTotal;
    }

    public static final class Builder {
        private OrderItemId orderItemId;
        private OrderId orderId;
        private Product product;
        private int quantity;
        private Money price;
        private Money subTotal;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder id(OrderItemId val) {
            orderItemId = val;
            return this;
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder product(Product val) {
            product = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder subTotal(Money val) {
            subTotal = val;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
