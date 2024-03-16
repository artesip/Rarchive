package com.example.rarchive.service;

import com.example.rarchive.entity.RecordEntity;
import com.example.rarchive.entity.RecordInfoEntity;
import com.example.rarchive.model.RecordInfoModel;
import com.example.rarchive.repository.RecordInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class RecordInfoService {
    @Autowired
    private RecordInfoRepository recordInfoRepository;

    @Autowired
    private RecordService recordService;

    public void save(RecordInfoEntity recordInfo) {
        recordInfoRepository.save(recordInfo);
    }

    public Collection<RecordInfoEntity> recordInfoEntityCollection(String login) {
        ArrayList<RecordEntity> recordEntities = (ArrayList<RecordEntity>) recordService.getCollectionRecords(login);

        Collection<RecordInfoEntity> recordInfoEntities = new ArrayList<>();
        for (var record : recordEntities) {
            recordInfoEntities.add(record.getRecordInfoEntity());
        }
        return recordInfoEntities;
    }

    public ResponseEntity<?> getRecordsInfo() {
        try {
            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            Collection<RecordInfoEntity> recordEntities = recordInfoEntityCollection(loginFromToken);

            return ResponseEntity.ok(recordEntities.stream().map(RecordInfoModel::toModel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    public ResponseEntity<?> addRecordInfo(long recordId, RecordInfoEntity recordInfo) {
        try {
            RecordEntity record = recordService.findById(recordId).orElseThrow(
                    () -> new Exception("Cant find record!\n")
            );
            recordInfo.setRecordEntity(record);
            save(recordInfo);
            record.setRecordInfoEntity(recordInfo);
            recordService.save(record);
            return ResponseEntity.ok("RecordInfo was successfully saved!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    public ResponseEntity<String> updateRecordInfo(RecordInfoEntity newRecordInfo) {
        try {
            RecordInfoEntity recordInfo = recordInfoRepository.findById(newRecordInfo.getId()).orElseThrow(
                    () -> new Exception("Cant find recordInfo with that id!\n")
            );
            if (!recordInfo.equals(newRecordInfo)) {
                save(newRecordInfo);
            }
            return ResponseEntity.ok("RecordInfo was successfully updated!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    public ResponseEntity<String> deleteRecordInfo(long id) {
        try {
            RecordInfoEntity recordInfo = recordInfoRepository.findById(id).orElseThrow(
                    () -> new Exception("Cant find recordInfo with this id!\n")
            );

            RecordEntity record = recordService.findByRecordInfoEntity(recordInfo).orElseThrow(
                    () -> new Exception("Cant find record!\n")
            );

            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            if (record.getGroupEntity().getUser().getLogin().equals(loginFromToken)) {
                recordInfoRepository.delete(recordInfo);
            } else {
                throw new Exception("You are trying to delete recordInfo from another user!\n");
            }

            if (recordInfoRepository.findById(id).isEmpty()) {
                return ResponseEntity.ok("RecordInfo was successfully deleted!\n");
            }

            return ResponseEntity.badRequest().body("Something went wrong!\n");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }
}
