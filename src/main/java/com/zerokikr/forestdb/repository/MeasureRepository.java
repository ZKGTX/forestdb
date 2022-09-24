package com.zerokikr.forestdb.repository;

import com.zerokikr.forestdb.entity.Measure;
import com.zerokikr.forestdb.entity.Subject;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MeasureRepository extends PagingAndSortingRepository<Measure, Long>, JpaSpecificationExecutor<Measure> {

    List<Measure> findByRiskIdOrderByNameAsc(Long id);
}
