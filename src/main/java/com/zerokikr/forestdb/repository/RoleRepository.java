package com.zerokikr.forestdb.repository;


import com.zerokikr.forestdb.entity.Action;
import com.zerokikr.forestdb.entity.Role;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
    Role findByName(String name);
}
