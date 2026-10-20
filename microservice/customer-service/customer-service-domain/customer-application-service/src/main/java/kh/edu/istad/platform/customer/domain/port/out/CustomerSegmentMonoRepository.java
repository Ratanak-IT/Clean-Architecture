package kh.edu.istad.platform.customer.domain.port.out;

import kh.edu.istad.platform.customer.domain.dto.CreateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerSegmentCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CustomerSegmentMonoRepository {
    Mono<CustomerSegmentResult> create(CreateCustomerSegmentCommand command);
    Flux<CustomerSegmentResult> findAll();
    Mono<CustomerSegmentResult> findById(String id);
    Mono<CustomerSegmentResult> update(String id, UpdateCustomerSegmentCommand command);
    Mono<Void> delete(String id);
}
