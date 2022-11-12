package com.zerokikr.forestdb.repository;

import com.zerokikr.forestdb.entity.ReportingYear;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ReportingYearRepository extends PagingAndSortingRepository<ReportingYear, Long>, JpaSpecificationExecutor<ReportingYear> {

    List<ReportingYear> findByActionIdOrderByYearAsc(Long id);

    ReportingYear findByYear(Integer year);
}
