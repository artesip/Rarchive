package com.example.rarchive.controller;

import com.example.rarchive.entity.RecordInfoEntity;
import com.example.rarchive.service.RecordInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/recordInfo")
public class RecordInfoController {
    private final RecordInfoService recordInfoService;

    @Autowired
    RecordInfoController(RecordInfoService recordInfoService) {
        this.recordInfoService = recordInfoService;
    }

    @GetMapping
    public ResponseEntity<?> getRecordsInfo() {
        return recordInfoService.getRecordsInfo();
    }

    @GetMapping
    public ResponseEntity<?> getRecordsInfoById(@RequestParam("id") long id) {
        return recordInfoService.getRecordsInfoById(id);
    }

    @PostMapping
    public ResponseEntity<?> addRecordInfo(@RequestParam(name = "record_id") long recordId, @RequestBody RecordInfoEntity recordInfo) {
        return recordInfoService.addRecordInfo(recordId, recordInfo);
    }

    @PutMapping
    public ResponseEntity<String> updateRecordEntity(@RequestBody RecordInfoEntity newRecordInfo) {
        return recordInfoService.updateRecordInfo(newRecordInfo);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteRecordInfo(@RequestParam(name = "record_info_id") long id) {
        return recordInfoService.deleteRecordInfo(id);
    }
}
