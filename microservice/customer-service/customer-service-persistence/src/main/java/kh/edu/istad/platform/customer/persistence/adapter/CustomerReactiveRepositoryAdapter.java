package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.common.domain.query.FilterQuery;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.persistence.query.MongoFilterBuilder;
import kh.edu.istad.platform.customer.domain.dto.*;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerMonoRepository;

import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerPersistenceMapper;
import kh.edu.istad.platform.customer.persistence.repository.CustomerMongoRepositoty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@RequiredArgsConstructor
@Repository
public class CustomerReactiveRepositoryAdapter implements CustomerMonoRepository {
    private final CustomerMongoRepositoty customerMongoRepositoty;
    private final MongoFilterBuilder filterBuilder;
    private final ReactiveMongoTemplate mongoTemplate;
//    private final CustomerSegmentRepository segmentRepository;
    private final CustomerPersistenceMapper mapper;

    private record StatusSegmentCount(CustomerStatus customerStatus, String segmentId, long total) {}

    @Override
    public Mono<CreateCustomerResult> save(Customer customer) {
        return customerMongoRepositoty.save(mapper.toDocument(customer))
                .map(mapper::toDomain);
    }

    @Override
    public Flux<GetCustomerResult> findAll() {
        return customerMongoRepositoty.findAll().map(mapper::toGetResult);
    }

    @Override
    public Mono<GetCustomerResult> findById(CustomerId id) {
        return customerMongoRepositoty.findById(id.value().toString())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer", id.value())))
                .map(mapper::toGetResult);
    }

    @Override
    public Mono<Void> deleteById(CustomerId id) {
        return customerMongoRepositoty.deleteById(id.value().toString()).switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer", id.value())));
    }

    @Override
    public Mono<GetCustomerResult> findByUsername(String userName) {
        return customerMongoRepositoty.findByUsername(userName)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer", userName)))
                .map(mapper::toGetResult);
    }

    @Override
    public Mono<UpdateCustomerResult> updateById(CustomerId id, UpdateCustomerByIdCommand updateCustomerByIdCommand) {
        return customerMongoRepositoty.findById(id.value().toString())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer", id.value())))
                .doOnNext(document -> mapper.updateDocument(updateCustomerByIdCommand, document))
                .flatMap(customerMongoRepositoty::save)
                .map(mapper::toUpdateResult);
    }

    @Override
    public Flux<GetCustomerResult> filter(FilterQuery query) {
        return filterBuilder.find(CustomerDocument.class, query, Sort.by(Sort.Direction.DESC, "createdAt"))
                .map(mapper::toGetResult);
    }


}
