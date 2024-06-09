package com.example.rarchive.service;

import com.example.rarchive.entity.RecordEntity;
import org.springframework.http.ResponseEntity;

public interface RecordService {

    ResponseEntity<?> getRecords();

    ResponseEntity<?> getRecordById(long id);

    ResponseEntity<?> addRecord(long groupId, RecordEntity r);

    ResponseEntity<String> updateRecord(RecordEntity r);

    ResponseEntity<String> deleteRecord(long id);

}
