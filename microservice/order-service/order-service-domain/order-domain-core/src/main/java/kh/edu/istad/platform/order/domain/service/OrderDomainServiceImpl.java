package kh.edu.istad.platform.order.domain.service;

import kh.edu.istad.platform.order.domain.entity.Business;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.domain.event.OrderCancelledEvent;
import kh.edu.istad.platform.order.domain.event.OrderCreatedEvent;
import kh.edu.istad.platform.order.domain.event.OrderPaidEvent;
import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderDomainServiceImpl implements OrderDomainService{

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Business business) {
        validateBusiness(order, business);
        setOrderProductInformation(order, business);
        order.initializeOrder();                       // validates, then PENDING + ids
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
    }

    private void validateBusiness(Order order, Business business) {
        if (business == null) {
            throw new OrderDomainException("Business must not be null");
        }
        if (!business.getId().equals(order.getBusinessId())) {
            throw new OrderDomainException("Order business " + order.getBusinessId().value()
                    + " does not match business " + business.getId().value());
        }
        if (!business.isActive()) {
            throw new OrderDomainException(
                    "Business with id " + business.getId().value() + " is currently not active");
        }
    }

    private void setOrderProductInformation(Order order, Business business) {
        for (OrderItem item : order.getItems()) {
            Product orderProduct = item.getProduct();
            Product confirmed = business.findProduct(orderProduct.getId())
                    .orElseThrow(() -> new OrderDomainException("Product " + orderProduct.getId().value()
                            + " does not belong to business " + business.getId().value()));
            orderProduct.updateWithConfirmedNameAndPrice(confirmed.getName(), confirmed.getPrice());
        }
    }
}
