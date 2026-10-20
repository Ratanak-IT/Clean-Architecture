package kh.edu.istad.platform.customer.domain.port.out;

import kh.edu.istad.common.domain.query.FilterQuery;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.*;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerMonoRepository {
    Mono<CreateCustomerResult> save(Customer customer);
    Flux<GetCustomerResult> findAll();
    Mono<GetCustomerResult> findById(CustomerId id);
    Mono<Void> deleteById(CustomerId id);
    Mono<GetCustomerResult> findByUsername(String userName);
    Mono<UpdateCustomerResult> updateById(CustomerId id, UpdateCustomerByIdCommand updateCustomerByIdCommand);
    Flux<GetCustomerResult> filter(FilterQuery query);
}
