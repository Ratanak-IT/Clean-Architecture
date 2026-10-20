package kh.edu.istad.platform.customer.persistence.repository;

import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerSegmentRepository extends ReactiveMongoRepository<CustomerSegmentDocument, String> {
    Mono<Boolean> existsByCode(String code);
}
