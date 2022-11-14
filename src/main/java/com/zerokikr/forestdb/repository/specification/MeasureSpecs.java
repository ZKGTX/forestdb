package com.zerokikr.forestdb.repository.specification;

import com.zerokikr.forestdb.entity.Measure;
import com.zerokikr.forestdb.entity.Risk;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class MeasureSpecs {

    public static Specification<Measure> nameContains (String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }

    public static Specification<Measure> riskIdEqualsTo (Long riskId) {
        return new Specification<Measure>() {
            @Override
            public Predicate toPredicate(Root<Measure> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("risk"), riskId);
            }
        };
    }
}
