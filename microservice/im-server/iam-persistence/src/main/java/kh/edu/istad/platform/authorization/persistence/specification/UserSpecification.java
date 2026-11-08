package kh.edu.istad.platform.authorization.persistence.specification;

import jakarta.persistence.criteria.Predicate;
import kh.edu.istad.platform.authorization.domain.dto.user.command.UserFilterQuery;
import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class UserSpecification {

    private UserSpecification() {}

    public static Specification<UserEntity> filter(UserFilterQuery query) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isFalse(root.get("isDeleted")));

            if (query.enabled() != null) {
                predicates.add(cb.equal(root.get("enabled"), query.enabled()));
            }

            if (query.emailVerified() != null) {
                predicates.add(cb.equal(root.get("emailVerified"), query.emailVerified()));
            }

            if (query.search() != null && !query.search().isBlank()) {
                String pattern = "%" + query.search().toLowerCase() + "%";
                Predicate usernameMatch = cb.like(cb.lower(root.get("username")), pattern);
                Predicate emailMatch = cb.like(cb.lower(root.get("email")), pattern);
                Predicate givenNameMatch = cb.like(cb.lower(root.get("givenName")), pattern);
                Predicate familyNameMatch = cb.like(cb.lower(root.get("familyName")), pattern);
                predicates.add(cb.or(usernameMatch, emailMatch, givenNameMatch, familyNameMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
