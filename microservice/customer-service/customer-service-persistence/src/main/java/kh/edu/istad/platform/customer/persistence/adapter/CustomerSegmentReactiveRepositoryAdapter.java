package kh.edu.istad.platform.customer.persistence.adapter;

import com.mongodb.DuplicateKeyException;
import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.exception.DuplicateException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentMonoRepository;
import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerSegmentPersistenceMapper;
import kh.edu.istad.platform.customer.persistence.repository.CustomerMongoRepositoty;
import kh.edu.istad.platform.customer.persistence.repository.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class CustomerSegmentReactiveRepositoryAdapter implements CustomerSegmentMonoRepository {

    private final CustomerSegmentRepository customerSegmentRepository;
    private final CustomerMongoRepositoty repositoty;
    private final ReactiveMongoTemplate mongoTemplate;
    private final CustomerSegmentPersistenceMapper mapper;
    @Override
    public Mono<CustomerSegmentResult> create(CreateCustomerSegmentCommand command) {
        return customerSegmentRepository.existsByCode(command.code())
                .filter(exit -> !exit)
                .switchIfEmpty(Mono.error(new DuplicateException("Customer segment code already exists")))
                .then(Mono.defer(() -> customerSegmentRepository.insert(mapper.toDomain(command))))
                .onErrorMap(DuplicateKeyException.class, e -> new DuplicateException("Customer segment code already exists: "+ command.code()))
                .map(mapper::toResult);
    }

    @Override
    public Flux<CustomerSegmentResult> findAll() {
        return customerSegmentRepository.findAll().map(mapper::toResult);
    }

    @Override
    public Mono<CustomerSegmentResult> findById(String id) {
        return customerSegmentRepository.findById(id).map(mapper::toResult);
    }

    @Override
    public Mono<CustomerSegmentResult> update(String id, UpdateCustomerSegmentCommand command) {
        return mongoTemplate.findAndModify(
                        Query.query(Criteria.where("id").is(id)),
                        new Update().set("code", command.code()).set("name", command.name()),
                        FindAndModifyOptions.options().returnNew(true),
                        CustomerSegmentDocument.class)
                .onErrorMap(DuplicateKeyException.class, e -> new DuplicateException("Customer segment code already exists: "+ command.code()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer", id)))
                .map(mapper::toResult);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repositoty.existsBySegmentId(id)
                .filter(inUse -> !inUse)
                .switchIfEmpty(Mono.error(new IllegalStateException("Customer segment is in use and cannot be deleted")))
                .then(Mono.defer(()-> mongoTemplate.findAndRemove(
                        Query.query(Criteria.where("id").is(id))
                        ,CustomerSegmentDocument.class
                )
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer", id)))))
                .then();

    }
}
