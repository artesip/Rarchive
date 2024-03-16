package com.example.rarchive.repository;

import com.example.rarchive.entity.GroupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface GroupRepository extends CrudRepository<GroupEntity, Long> {

    Collection<GroupEntity> findAllByUserId(long id);
}
