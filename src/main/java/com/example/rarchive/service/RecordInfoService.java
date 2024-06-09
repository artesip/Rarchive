package com.example.rarchive.service;

import com.example.rarchive.entity.RecordInfoEntity;
import org.springframework.http.ResponseEntity;

public interface RecordInfoService {

    ResponseEntity<?> getRecordsInfo();

    ResponseEntity<?> getRecordsInfoById(long id);

    ResponseEntity<?> addRecordInfo(long recordId, RecordInfoEntity recordInfo);

    ResponseEntity<String> updateRecordInfo(RecordInfoEntity newRecordInfo);

    ResponseEntity<String> deleteRecordInfo(long id);

}
