package com.recruvia.backend.repository.specification;

import com.recruvia.backend.entity.User;
import com.recruvia.backend.enums.AccountStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class UserSpecification {

    private UserSpecification() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Builds a Specification that filters users by keyword (firstName, lastName, email),
     * role name, account status, and email verification status.
     */
    public static Specification<User> filter(
            String keyword,
            String role,
            AccountStatus status,
            Boolean emailVerified
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), pattern),
                        cb.like(cb.lower(root.get("lastName")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern)
                ));
            }

            if (role != null && !role.isBlank()) {
                predicates.add(cb.equal(
                        cb.lower(root.join("role").get("name")),
                        role.toLowerCase()
                ));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("accountStatus"), status));
            }

            if (emailVerified != null) {
                predicates.add(cb.equal(root.get("emailVerified"), emailVerified));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
