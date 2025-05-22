package com.arthur.tasktrackerapi.task.repository;

import com.arthur.tasktrackerapi.task.dto.filter.TaskFilterRequest;
import com.arthur.tasktrackerapi.task.entity.Task;
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
public class TaskRepositoryImpl implements TaskRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Page<Task> findAllFiltered(Long projectId, TaskFilterRequest filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);
        Root<Task> root = cq.from(Task.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("project").get("id"), projectId));
        predicates.add(cb.equal(root.get("archived"), false));

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getPriority() != null) {
            predicates.add(cb.equal(root.get("priority"), filter.getPriority()));
        }

        if (filter.getDeadline() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("deadline"), filter.getDeadline()));
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

        TypedQuery<Task> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Task> resultList = query.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Task> countRoot = countQuery.from(Task.class);
        countQuery.select(cb.count(countRoot));

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.equal(countRoot.get("project").get("id"), projectId));
        countPredicates.add(cb.equal(countRoot.get("archived"), false));

        if (filter.getStatus() != null) {
            countPredicates.add(cb.equal(countRoot.get("status"), filter.getStatus()));
        }

        if (filter.getPriority() != null) {
            countPredicates.add(cb.equal(countRoot.get("priority"), filter.getPriority()));
        }

        if (filter.getDeadline() != null) {
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("deadline"), filter.getDeadline()));
        }

        countQuery.where(countPredicates.toArray(new Predicate[0]));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, count);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Task> root, Long projectId, TaskFilterRequest filter) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("project").get("id"), projectId));
        predicates.add(cb.equal(root.get("archived"), false));

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getPriority() != null) {
            predicates.add(cb.equal(root.get("priority"), filter.getPriority()));
        }

        if (filter.getDeadline() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("deadline"), filter.getDeadline()));
        }

        return predicates;
    }

}
