package com.zerokikr.forestdb.repository;

import com.zerokikr.forestdb.entity.Risk;
import com.zerokikr.forestdb.entity.Subject;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RiskRepository extends PagingAndSortingRepository<Risk, Long>, JpaSpecificationExecutor<Risk> {

    List<Risk> findBySubjectIdOrderByNameAsc(Long id);

    @Query(value = "SELECT * FROM risks WHERE subject_id = subjectId AND name LIKE %keyword% ORDER BY name ASC", nativeQuery = true)
    List<Risk> findBySubjectIdAndKeywordOrderByNameAsc(@Param("subjectId") Long subjectId, @Param("keyword") String keyword);

}
