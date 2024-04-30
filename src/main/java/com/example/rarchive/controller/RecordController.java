package com.example.rarchive.controller;

import com.example.rarchive.entity.RecordEntity;
import com.example.rarchive.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/record")
public class RecordController {
    private final RecordService recordService;

    @Autowired
    RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public ResponseEntity<?> getRecords() {
        return recordService.getRecords();
    }

    @PostMapping
    public ResponseEntity<?> addRecord(@RequestParam(name = "group_id") long groupId, @RequestBody RecordEntity record) {
        return recordService.addRecord(groupId, record);
    }

    @PutMapping
    public ResponseEntity<String> updateRecord(@RequestBody RecordEntity record) {
        return recordService.updateRecord(record);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteRecord(@RequestParam(name = "record_id") long id) {
        return recordService.deleteRecord(id);
    }
}
