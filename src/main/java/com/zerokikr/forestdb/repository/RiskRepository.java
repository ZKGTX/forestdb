package com.zerokikr.forestdb.repository;

import com.zerokikr.forestdb.entity.Risk;
import com.zerokikr.forestdb.entity.Subject;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RiskRepository extends PagingAndSortingRepository<Risk, Long>, JpaSpecificationExecutor<Risk> {

    List<Risk> findBySubjectIdOrderByNameAsc(Long id);


}
