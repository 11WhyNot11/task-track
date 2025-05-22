package com.arthur.tasktrackerapi.project.repository;

import com.arthur.tasktrackerapi.project.dto.filter.ProjectFilterRequest;
import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.task.entity.Task;
import com.arthur.tasktrackerapi.user.entity.User;
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
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Page<Project> getAllByOwnerFiltered(User owner, ProjectFilterRequest filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> cq = cb.createQuery(Project.class);
        Root<Project> root = cq.from(Project.class);

        List<Predicate> predicates = buildPredicates(owner, filter, cb, root);
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

        TypedQuery<Project> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Project> resultList = query.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Project> countRoot = countQuery.from(Project.class);
        countQuery.select(cb.count(countRoot));
        List<Predicate> countPredicates = buildPredicates(owner, filter, cb, countRoot);
        countQuery.where(countPredicates.toArray(new Predicate[0]));

        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, count);
    }

    private List<Predicate> buildPredicates(User owner, ProjectFilterRequest filter, CriteriaBuilder cb, Root<Project> root) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("owner"), owner));
        predicates.add(cb.equal(root.get("archived"), false));

        if (filter.getName() != null) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }

        if (filter.getOwnerId() != null) {
            predicates.add(cb.equal(root.get("owner").get("id"), filter.getOwnerId()));
        }

        return predicates;
    }
}
