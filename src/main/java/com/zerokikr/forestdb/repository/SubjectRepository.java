package com.zerokikr.forestdb.repository;

import com.zerokikr.forestdb.entity.Subject;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends PagingAndSortingRepository <Subject, Long>, JpaSpecificationExecutor<Subject> {

    public List<Subject> findAllByOrderByNameAsc();




}
