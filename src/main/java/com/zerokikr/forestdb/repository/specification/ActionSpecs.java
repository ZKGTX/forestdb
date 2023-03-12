package com.zerokikr.forestdb.repository.specification;

import com.zerokikr.forestdb.entity.Action;
import com.zerokikr.forestdb.entity.Measure;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ActionSpecs {
    public static Specification<Action> nameContains (String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword + "%");
    }

    public static Specification<Action> measureIdEqualsTo (Long measureId) {
        return new Specification<Action>() {
            @Override
            public Predicate toPredicate(Root<Action> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("measure"), measureId);
            }
        };
    }
}
