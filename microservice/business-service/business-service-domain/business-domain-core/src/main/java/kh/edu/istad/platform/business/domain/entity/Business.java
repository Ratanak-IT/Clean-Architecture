package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderStatus;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import kh.edu.istad.platform.business.domain.validation.Guard;
import kh.edu.istad.platform.business.domain.valueobject.BusinessContact;
import kh.edu.istad.platform.business.domain.valueobject.BusinessName;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalId;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalStatus;

import java.util.*;

public class Business extends AggregateRoot<BusinessId> {
    private final BusinessName businessName;
    private final BusinessContact businessContact;
    private boolean active;
    private final List<Product> products;
    private OrderApproval orderApproval;
    private final OrderDetail orderDetail;

    private Business(Builder builder) {
        setId(builder.businessId);
        this.businessName = Guard.notNull(builder.businessName, "Business name");
        this.businessContact = Guard.notNull(builder.businessContact, "Business contact");
        this.active = builder.active;
        this.products = builder.products == null ? new ArrayList<>() : new ArrayList<>(builder.products);
        this.orderApproval = builder.orderApproval;
        this.orderDetail = builder.orderDetail;
    }


    public void initializeBusiness() {
        validateBusiness();
        setId(new BusinessId(UUID.randomUUID()));
        active = true;
        products.forEach(Product::initialize);
    }

    public void validateBusiness() {
        if (getId() != null) {
            throw new BusinessDomainException("Business is not in correct state for initialization");
        }
        Set<String> names = new HashSet<>();
        for (Product product : products) {
            product.validate();
            if (!names.add(product.getName().toLowerCase(Locale.ROOT))) {
                throw new BusinessDomainException("Duplicate product name '" + product.getName() + "'");
            }
        }
    }


    public void validateOrder(List<String> failureMessages) {
        Guard.notNull(failureMessages, "Failure messages");
        if (orderDetail == null) {
            // nothing to approve or reject: this is a programming/flow error, not a business decision
            throw new BusinessDomainException("There is no order to validate");
        }
        if (!active) {
            failureMessages.add("Business " + idText() + " is not active");
        }
        if (orderDetail.getOrderStatus() != OrderStatus.PAID) {
            failureMessages.add("Payment is not completed for order " + orderDetail.getId().value());
        }
        Money computedTotal = Money.ZERO;
        for (Product ordered : orderDetail.getProducts()) {
            Optional<Product> found = findProduct(ordered.getId());
            if (found.isEmpty()) {
                failureMessages.add("Product " + ordered.getId().value() + " does not belong to this business");
                continue;
            }
            Product catalog = found.get();
            if (!catalog.isAvailable()) {
                failureMessages.add("Product '" + catalog.getName() + "' is not available");
            } else if (!catalog.hasStockFor(ordered.getQuantity())) {
                failureMessages.add("Not enough stock for product '" + catalog.getName() + "'");
            }
            if (!catalog.getPrice().equals(ordered.getPrice())) {
                failureMessages.add("Price of product '" + catalog.getName() + "' is "
                        + catalog.getPrice().amount() + " but the order has " + ordered.getPrice().amount());
            }
            computedTotal = computedTotal.add(catalog.getPrice().multiply(ordered.getQuantity()));
        }
        if (!computedTotal.equals(orderDetail.getTotalAmount())) {
            failureMessages.add("Total amount " + orderDetail.getTotalAmount().amount()
                    + " does not match the computed total " + computedTotal.amount());
        }
    }

    public void constructOrderApproval(OrderApprovalStatus approvalStatus) {
        if (orderDetail == null) {
            throw new BusinessDomainException("Cannot construct an order approval without an order detail");
        }
        this.orderApproval = new OrderApproval(OrderApprovalId.newId(), getId(), orderDetail.getId(), approvalStatus);
    }

    public Optional<Product> findProduct(ProductId productId) {
        return products.stream().filter(product -> product.getId().equals(productId)).findFirst();
    }

    private String idText() {
        return getId() == null ? "(not registered)" : String.valueOf(getId().value());
    }


    public BusinessName getBusinessName() {
        return businessName;
    }

    public BusinessContact getBusinessContact() {
        return businessContact;
    }

    public boolean isActive() {
        return active;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }


    public static final class Builder {
        private BusinessId businessId;
        private BusinessName businessName;
        private BusinessContact businessContact;
        private boolean active;
        private List<Product> products;
        private OrderApproval orderApproval;
        private OrderDetail orderDetail;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder id(BusinessId val) {
            businessId = val;
            return this;
        }

        public Builder businessName(BusinessName val) {
            businessName = val;
            return this;
        }

        public Builder businessContact(BusinessContact val) {
            businessContact = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder orderApproval(OrderApproval val) {
            orderApproval = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public Business build() {
            return new Business(this);
        }
    }
}
