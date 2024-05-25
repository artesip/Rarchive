package com.example.rarchive.repository;

import com.example.rarchive.entity.RecordInfoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordInfoRepository extends CrudRepository<RecordInfoEntity, Long> {
}
