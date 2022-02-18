package io.coolexplorer.auth.specification;

import io.coolexplorer.auth.filter.AccountSearchFilter;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AccountListSpecification implements Specification<Account> {
    private final AccountSearchFilter filter;

    @Override
    public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return new PredicateBuilder(root, criteriaBuilder, criteriaQuery)
                .addSearchQuery(filter.getSearchField(), filter.getSearchQuery())
                .addRole(filter.getRoleId())
                .addOrder(filter.getOrderBy(), filter.getOrder())
                .build();
    }

    public static class PredicateBuilder {
        private final Root<Account> root;
        private final CriteriaBuilder criteriaBuilder;
        private final CriteriaQuery<?> criteriaQuery;
        private final Join<Account, Role> accountRoleJoin;
        private final List<Predicate> predicates;


        public PredicateBuilder(Root<Account> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery) {
            this.root = root;
            this.criteriaBuilder = criteriaBuilder;
            this.criteriaQuery = criteriaQuery;
            this.accountRoleJoin = root.join("roles");
            this.predicates = new ArrayList<>();
        }

        public PredicateBuilder addSearchQuery(String searchField, String searchQuery) {
            if (StringUtils.isNotEmpty(searchField) && StringUtils.isNotEmpty(searchQuery)) {
                predicates.add(criteriaBuilder.like(root.get(searchField), "%" + searchQuery + "%"));
            }
            return this;
        }

        public PredicateBuilder addRole(Long roleId) {
            if (roleId != null) {
                predicates.add(criteriaBuilder.equal(accountRoleJoin.get("id"), roleId));
            }
            return this;
        }

        public PredicateBuilder addOrder(String orderBy, String order) {
            if (StringUtils.isNotEmpty(orderBy) && StringUtils.isNotEmpty(order)) {
                if (order.equalsIgnoreCase("asc")) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(orderBy)));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderBy)));
                }
            }
            return this;
        }

        public Predicate build() {
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }
    }
}
