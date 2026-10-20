package kh.edu.istad.platform.customer.persistence.adapter;


import kh.edu.istad.platform.customer.domain.dto.CustomerStatisticResult;
import kh.edu.istad.platform.customer.domain.dto.SegmentTotal;
import kh.edu.istad.platform.customer.domain.dto.StatusCount;
import kh.edu.istad.platform.customer.domain.port.out.CustomerStatisticsRepository;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
@Repository
public class CustomerStatisticsReactiveRepositoryAdapter implements CustomerStatisticsRepository {
    private final ReactiveMongoTemplate mongoTemplate;
    @Override
    public Mono<CustomerStatisticResult> getStatistics() {
        String customers = mongoTemplate.getCollectionName(CustomerDocument.class);
        String segments = mongoTemplate.getCollectionName(CustomerSegmentDocument.class);

        Aggregation statusAgg = newAggregation(
                group("customerStatus").count().as("total")
        );

        Mono<List<StatusCount>> byStatus = mongoTemplate
                .aggregate(statusAgg, customers, Document.class)
                .map(doc -> new StatusCount(
                        String.valueOf(doc.get("_id")),
                        doc.get("total", Number.class).longValue()))
                .collectList();

        Aggregation segmentAgg = newAggregation(
                Aggregation.stage(new Document("$lookup", new Document()
                        .append("from", segments)
                        .append("localField", "segment")
                        .append("foreignField", "_id")
                        .append("as", "seg"))),
                Aggregation.stage(new Document("$unwind", new Document()
                        .append("path", "$seg")
                        .append("preserveNullAndEmptyArrays", true))),
                Aggregation.stage(new Document("$group", new Document()
                        .append("_id", new Document("$ifNull", List.of("$seg.name", "No segment")))
                        .append("total", new Document("$sum", 1))))
        );

        Mono<List<SegmentTotal>> bySegment = mongoTemplate
                .aggregate(segmentAgg, customers, Document.class)
                .map(doc -> new SegmentTotal(
                        String.valueOf(doc.get("_id")),
                        doc.get("total", Number.class).longValue()))
                .collectList();

        return Mono.zip(mongoTemplate.count(new Query(), customers), byStatus, bySegment)
                .map(t -> new CustomerStatisticResult(t.getT1(), t.getT2(), t.getT3()));
    }
}
