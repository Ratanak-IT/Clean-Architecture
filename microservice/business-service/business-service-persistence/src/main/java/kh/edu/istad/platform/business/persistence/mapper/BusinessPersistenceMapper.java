package kh.edu.istad.platform.business.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.business.domain.entity.Business;
import kh.edu.istad.platform.business.domain.entity.Product;
import kh.edu.istad.platform.business.domain.valueobject.BusinessContact;
import kh.edu.istad.platform.business.domain.valueobject.BusinessName;
import kh.edu.istad.platform.business.persistence.document.BusinessDocument;
import kh.edu.istad.platform.business.persistence.document.ProductDocument;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class BusinessPersistenceMapper {

    public BusinessDocument toDocument(Business business) {
        BusinessDocument document = new BusinessDocument();
        document.setId(business.getId().value().toString());
        document.setLegalName(business.getBusinessName().legalName());
        document.setDisplayName(business.getBusinessName().displayName());
        document.setEmail(business.getBusinessContact().email());
        document.setPhoneNumber(business.getBusinessContact().phoneNumber());
        document.setActive(business.isActive());
        List<ProductDocument> products = new ArrayList<>();
        for (Product product : business.getProducts()) {
            ProductDocument productDocument = new ProductDocument();
            productDocument.setId(product.getId().value().toString());
            productDocument.setName(product.getName());
            productDocument.setPrice(product.getPrice().amount());
            productDocument.setQuantity(product.getQuantity());
            productDocument.setAvailable(product.isAvailable());
            products.add(productDocument);
        }
        document.setProducts(products);
        return document;
    }

    public Business toDomain(BusinessDocument document) {
        List<Product> products = document.getProducts().stream()
                .map(productDocument -> new Product(
                        new ProductId(UUID.fromString(productDocument.getId())),
                        productDocument.getName(),
                        new Money(productDocument.getPrice()),
                        productDocument.getQuantity(),
                        productDocument.isAvailable()))
                .toList();
        return Business.Builder.newBuilder()
                .id(new BusinessId(UUID.fromString(document.getId())))
                .businessName(new BusinessName(document.getLegalName(), document.getDisplayName()))
                .businessContact(new BusinessContact(document.getEmail(), document.getPhoneNumber()))
                .active(document.isActive())
                .products(products)
                .build();
    }
}
