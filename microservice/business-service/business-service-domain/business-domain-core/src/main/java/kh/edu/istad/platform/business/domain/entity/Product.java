package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import kh.edu.istad.platform.business.domain.validation.Guard;

import java.util.UUID;


public class Product extends BaseEntity<ProductId> {

    public static final int MAX_NAME_LENGTH = 100;

    private final String name;
    private final Money price;
    private final int quantity;
    private boolean available;

    public Product(ProductId productId, String name, Money price, int quantity, boolean available) {
        setId(productId);
        this.name = name == null ? null : name.trim();
        this.price = price;
        this.quantity = quantity;
        this.available = available;
    }

    public static Product forRegistration(String name, Money price, int quantity) {
        return new Product(null, name, price, quantity, false);
    }

    void initialize() {
        setId(new ProductId(UUID.randomUUID()));
        available = quantity > 0;
    }

    void validate() {
        Guard.text(name, "Product name", MAX_NAME_LENGTH);
        Guard.notNull(price, "Price of product '" + name + "'");
        if (!price.isGreaterThanZero()) {
            throw new BusinessDomainException("Price of product '" + name + "' must be greater than zero");
        }
        if (quantity < 0) {
            throw new BusinessDomainException("Quantity of product '" + name + "' must not be negative");
        }
    }

    public boolean hasStockFor(int orderedQuantity) {
        return available && quantity >= orderedQuantity;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return available;
    }
}
