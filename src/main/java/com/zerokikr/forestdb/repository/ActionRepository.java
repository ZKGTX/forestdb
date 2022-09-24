package com.zerokikr.forestdb.repository;

import com.zerokikr.forestdb.entity.Action;
import com.zerokikr.forestdb.entity.Measure;
import com.zerokikr.forestdb.entity.Subject;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ActionRepository extends PagingAndSortingRepository<Action, Long>, JpaSpecificationExecutor<Action> {

    List<Action> findByMeasureIdOrderByNameAsc(Long id);
}
