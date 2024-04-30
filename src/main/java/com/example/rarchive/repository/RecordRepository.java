package com.example.rarchive.repository;

import com.example.rarchive.entity.GroupEntity;
import com.example.rarchive.entity.RecordEntity;
import com.example.rarchive.entity.RecordInfoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends CrudRepository<RecordEntity, Long> {
    List<RecordEntity> findAllByGroupEntity(GroupEntity group);

    Optional<RecordEntity> findByRecordInfoEntity(RecordInfoEntity recordInfo);
}
