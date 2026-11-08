package kh.edu.istad.platform.authorization.persistence.specification;

import jakarta.persistence.criteria.Predicate;
import kh.edu.istad.platform.authorization.domain.dto.role.command.RoleFilterQuery;
import kh.edu.istad.platform.authorization.persistence.entity.RoleEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class RoleSpecification {

    private RoleSpecification() {}

    public static Specification<RoleEntity> filter(RoleFilterQuery query) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isFalse(root.get("isDeleted")));

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
