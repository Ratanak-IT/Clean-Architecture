package kh.edu.istad.platform.business.restapi.mapper;

import kh.edu.istad.platform.business.domain.dto.BusinessResult;
import kh.edu.istad.platform.business.domain.dto.ProductCommand;
import kh.edu.istad.platform.business.domain.dto.RegisterBusinessCommand;
import kh.edu.istad.platform.business.restapi.dto.BusinessResponse;
import kh.edu.istad.platform.business.restapi.dto.ProductResponse;
import kh.edu.istad.platform.business.restapi.dto.RegisterBusinessRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusinessWebMapper {

    public RegisterBusinessCommand toCommand(RegisterBusinessRequest request) {
        List<ProductCommand> products = request.products() == null ? List.of() : request.products().stream()
                .map(product -> new ProductCommand(product.name(), product.price(), product.quantity()))
                .toList();
        return new RegisterBusinessCommand(
                request.legalName(), request.displayName(), request.email(), request.phoneNumber(), products);
    }

    public BusinessResponse toResponse(BusinessResult result) {
        return new BusinessResponse(
                result.businessId(),
                result.legalName(),
                result.displayName(),
                result.email(),
                result.phoneNumber(),
                result.active(),
                result.products().stream()
                        .map(product -> new ProductResponse(product.productId(), product.name(),
                                product.price(), product.quantity(), product.available()))
                        .toList());
    }
}
