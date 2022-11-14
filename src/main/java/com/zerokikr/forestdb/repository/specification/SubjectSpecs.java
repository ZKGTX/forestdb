package com.zerokikr.forestdb.repository.specification;

import com.zerokikr.forestdb.entity.Subject;
import org.springframework.data.jpa.domain.Specification;

public class SubjectSpecs {

public static Specification<Subject> nameContains (String keyword) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
}
}
