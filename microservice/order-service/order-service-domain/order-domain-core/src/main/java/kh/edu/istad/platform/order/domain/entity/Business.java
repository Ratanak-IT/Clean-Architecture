package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.ProductId;

import java.util.List;
import java.util.Optional;

public class Business extends AggregateRoot<BusinessId> {
    private final List<Product> products;
    private boolean active;

    public List<Product> getProducts() {
        return products;
    }
    public boolean getActive(){
        return active;
    }

    public Business(BusinessId businessId, List<Product> products, boolean active) {
        super.setId(businessId);
        this.products = products == null ? List.of() : List.copyOf(products);
        this.active = active;
    }

    public Optional<Product> findProduct(ProductId productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();
    }

    public boolean isActive() {
        return active;
    }




}