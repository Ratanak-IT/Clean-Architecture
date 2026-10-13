package kh.edu.istad.platform.business.domain.mapper;

import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.platform.business.domain.dto.BusinessResult;
import kh.edu.istad.platform.business.domain.dto.ProductCommand;
import kh.edu.istad.platform.business.domain.dto.ProductResult;
import kh.edu.istad.platform.business.domain.dto.RegisterBusinessCommand;
import kh.edu.istad.platform.business.domain.entity.Business;
import kh.edu.istad.platform.business.domain.entity.Product;
import kh.edu.istad.platform.business.domain.valueobject.BusinessContact;
import kh.edu.istad.platform.business.domain.valueobject.BusinessName;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusinessMapper {

    public Business toBusiness(RegisterBusinessCommand command) {
        List<ProductCommand> productCommands = command.products() == null ? List.of() : command.products();
        List<Product> products = productCommands.stream()
                .map(product -> Product.forRegistration(
                        product.name(),
                        product.price() == null ? null : new Money(product.price()),
                        product.quantity()))
                .toList();
        return Business.Builder.newBuilder()
                .businessName(new BusinessName(command.legalName(), command.displayName()))
                .businessContact(new BusinessContact(command.email(), command.phoneNumber()))
                .products(products)
                .build();
    }

    public BusinessResult toResult(Business business) {
        return new BusinessResult(
                business.getId().value(),
                business.getBusinessName().legalName(),
                business.getBusinessName().displayName(),
                business.getBusinessContact().email(),
                business.getBusinessContact().phoneNumber(),
                business.isActive(),
                business.getProducts().stream()
                        .map(product -> new ProductResult(
                                product.getId().value(),
                                product.getName(),
                                product.getPrice().amount(),
                                product.getQuantity(),
                                product.isAvailable()))
                        .toList());
    }
}
