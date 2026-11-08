package kh.edu.istad.platform.authorization.persistence.specification;

import jakarta.persistence.criteria.Predicate;
import kh.edu.istad.platform.authorization.domain.dto.permission.command.PermissionFilterQuery;
import kh.edu.istad.platform.authorization.persistence.entity.PermissionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class PermissionSpecification {

    private PermissionSpecification() {}

    public static Specification<PermissionEntity> filter(PermissionFilterQuery query) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isFalse(root.get("isDeleted")));

            if (query.resource() != null && !query.resource().isBlank()) {
                predicates.add(cb.equal(root.get("resource"), query.resource().toLowerCase()));
            }

            if (query.action() != null && !query.action().isBlank()) {
                predicates.add(cb.equal(root.get("action"), query.action().toLowerCase()));
            }

            if (query.active() != null) {
                predicates.add(cb.equal(root.get("isActive"), query.active()));
            }

            if (query.system() != null) {
                predicates.add(cb.equal(root.get("isSystem"), query.system()));
            }

            if (query.search() != null && !query.search().isBlank()) {
                String pattern = "%" + query.search().toLowerCase() + "%";
                Predicate nameMatch = cb.like(cb.lower(root.get("name")), pattern);
                Predicate descMatch = cb.like(cb.lower(root.get("description")), pattern);
                predicates.add(cb.or(nameMatch, descMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
