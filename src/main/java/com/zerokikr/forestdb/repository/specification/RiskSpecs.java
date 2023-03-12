package com.zerokikr.forestdb.repository.specification;

import com.zerokikr.forestdb.entity.Risk;
import com.zerokikr.forestdb.entity.Subject;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class RiskSpecs {

    public static Specification<Risk> nameContains (String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword + "%");
    }

    public static Specification<Risk> subjectIdEqualsTo (Long subjectId) {
        return new Specification<Risk>() {
            @Override
            public Predicate toPredicate(Root<Risk> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("subject"), subjectId);
            }
        };
    }


}
