package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.ProductId;

import java.util.List;
import java.util.Optional;

public class Business extends AggregateRoot<BusinessId> {
    private final List<Product> products;
    private final boolean active;

    private Business(Builder builder) {
        setId(builder.businessId);
        this.products = builder.products == null ? List.of() : List.copyOf(builder.products);
        this.active = builder.active;
    }

    public Optional<Product> findProduct(ProductId productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();
    }

    public boolean isActive() {
        return active;
    }

    public List<Product> getProducts() {
        return products;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private BusinessId businessId;
        private List<Product> products;
        private boolean active;

        private Builder() {
        }

        public Builder businessId(BusinessId businessId) {
            this.businessId = businessId;
            return this;
        }

        public Builder products(List<Product> products) {
            this.products = products;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Business build() {
            return new Business(this);
        }
    }
}
