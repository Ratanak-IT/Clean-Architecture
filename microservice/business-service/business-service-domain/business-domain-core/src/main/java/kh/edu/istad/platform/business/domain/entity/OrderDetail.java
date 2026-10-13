package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.OrderStatus;

import java.util.Collections;
import java.util.List;

public class OrderDetail extends BaseEntity<OrderId> {

    private final OrderStatus orderStatus;
    private final Money totalAmount;
    private final List<Product> products;

    public OrderDetail(OrderId orderId, OrderStatus orderStatus, Money totalAmount, List<Product> products) {
        setId(orderId);
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.products = products == null ? List.of() : List.copyOf(products);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
}
