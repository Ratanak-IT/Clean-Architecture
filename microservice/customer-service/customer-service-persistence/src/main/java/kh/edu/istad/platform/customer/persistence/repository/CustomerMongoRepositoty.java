package kh.edu.istad.platform.customer.persistence.repository;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.GetCustomerResult;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerMongoRepositoty extends ReactiveMongoRepository<CustomerDocument, String> {

    @Query("{username:  {$eq:  ?0}}")
    Mono<CustomerDocument> findByUsername(String username);
    @Query(value = "{ 'segment.$id': ?0 }", exists = true)
    Mono<Boolean> existsBySegmentId(String segmentId);

}