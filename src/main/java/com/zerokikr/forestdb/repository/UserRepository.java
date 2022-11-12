package com.zerokikr.forestdb.repository;

import com.zerokikr.forestdb.entity.ReportingYear;
import com.zerokikr.forestdb.entity.Subject;
import com.zerokikr.forestdb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    public List<User> findAllByOrderByEmailAsc();

    public User findByEmail(String email);



}
