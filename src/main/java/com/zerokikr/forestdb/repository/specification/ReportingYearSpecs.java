package com.zerokikr.forestdb.repository.specification;

import com.zerokikr.forestdb.entity.ReportingYear;
import com.zerokikr.forestdb.entity.Risk;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ReportingYearSpecs {

    public static Specification<ReportingYear> yearEqualsTo (Integer year) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("year"), year);
    }

    public static Specification<ReportingYear> actionIdEqualsTo (Long actionId) {
        return new Specification<ReportingYear>() {
            @Override
            public Predicate toPredicate(Root<ReportingYear> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("action"), actionId);
            }
        };
    }
}
