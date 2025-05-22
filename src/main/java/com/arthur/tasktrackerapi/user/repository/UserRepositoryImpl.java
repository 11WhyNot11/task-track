package com.arthur.tasktrackerapi.user.repository;

import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.dto.filter.UserFilterRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Page<User> findAllFiltered(UserFilterRequest filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getFirstName() != null) {
            predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + filter.getFirstName().toLowerCase() + "%"));
        }

        if (filter.getLastName() != null) {
            predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + filter.getLastName().toLowerCase() + "%"));
        }

        if (filter.getEmail() != null) {
            predicates.add(cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                try {
                    Path<Object> path = root.get(order.getProperty());
                    orders.add(order.isAscending() ? cb.asc(path) : cb.desc(path));
                } catch (IllegalArgumentException e) {
                    System.out.println("⚠️ Unknown sort field: " + order.getProperty());
                }
            });
            cq.orderBy(orders);
        }

        TypedQuery<User> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<User> resultList = query.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        countQuery.select(cb.count(countRoot));

        List<Predicate> countPredicates = new ArrayList<>();

        if (filter.getFirstName() != null) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("firstName")), "%" + filter.getFirstName().toLowerCase() + "%"));
        }

        if (filter.getLastName() != null) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("lastName")), "%" + filter.getLastName().toLowerCase() + "%"));
        }

        if (filter.getEmail() != null) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
        }

        countQuery.where(countPredicates.toArray(new Predicate[0]));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, count);
    }
}
