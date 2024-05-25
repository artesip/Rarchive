package com.example.rarchive.service.impl;

import com.example.rarchive.entity.RecordEntity;
import com.example.rarchive.entity.RecordInfoEntity;
import com.example.rarchive.exception.NoSuchDataException;
import com.example.rarchive.exception.UnauthorizedAccessException;
import com.example.rarchive.model.RecordInfoModel;
import com.example.rarchive.repository.RecordInfoRepository;
import com.example.rarchive.service.RecordInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@Primary
public class RecordInfoServiceImpl implements RecordInfoService {
    @Autowired
    private RecordInfoRepository recordInfoRepository;

    @Autowired
    private RecordServiceImpl recordServiceImpl;

    public void save(RecordInfoEntity recordInfo) {
        recordInfoRepository.save(recordInfo);
    }

    public Collection<RecordInfoEntity> recordInfoEntityCollection(String login) {
        ArrayList<RecordEntity> recordEntities = (ArrayList<RecordEntity>) recordServiceImpl.getListRecords(login);

        return recordEntities.stream().collect(ArrayList::new,
                (list, record) -> list.add(record.getRecordInfoEntity()),
                ArrayList::addAll
        );
    }

    public Optional<RecordInfoEntity> getRecordInfoEntityById(long id) {
        return recordInfoRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> getRecordsInfo() {
        try {
            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            Collection<RecordInfoEntity> recordEntities = recordInfoEntityCollection(loginFromToken);

            return ResponseEntity.ok(recordEntities.stream().map(RecordInfoModel::toModel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getRecordsInfoById(long id) {
        try {
            return ResponseEntity.ok(getRecordInfoEntityById(id).orElseThrow(NoSuchDataException::new));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addRecordInfo(long recordId, RecordInfoEntity recordInfo) {
        try {
            RecordEntity record = recordServiceImpl.findById(recordId).orElseThrow(
                    () -> new NoSuchDataException("Cant find record!\n")
            );
            recordInfo.setRecordEntity(record);
            save(recordInfo);
            record.setRecordInfoEntity(recordInfo);
            recordServiceImpl.save(record);

            return ResponseEntity.ok("RecordInfo was successfully saved!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> updateRecordInfo(RecordInfoEntity newRecordInfo) {
        try {
            RecordInfoEntity recordInfo = recordInfoRepository.findById(newRecordInfo.getId()).orElseThrow(
                    () -> new NoSuchDataException("Cant find recordInfo with that id!\n")
            );

            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            if (!recordInfo.getRecordEntity().getGroupEntity().getUser().getLogin().equals(loginFromToken)) {
                throw new UnauthorizedAccessException("You are trying to update record from another user!\n");
            }
            save(newRecordInfo);

            return ResponseEntity.ok("RecordInfo was successfully updated!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteRecordInfo(long id) {
        try {
            RecordInfoEntity recordInfo = recordInfoRepository.findById(id).orElseThrow(
                    () -> new NoSuchDataException("Cant find recordInfo with this id!\n")
            );

            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            if (!recordInfo.getRecordEntity().getGroupEntity().getUser().getLogin().equals(loginFromToken)) {
                throw new UnauthorizedAccessException("You are trying to delete recordInfo from another user!\n");
            }
            recordInfoRepository.delete(recordInfo);

            if (recordInfoRepository.findById(id).isPresent()) {
                return ResponseEntity.badRequest().body("Something went wrong!\n");
            }

            return ResponseEntity.ok("RecordInfo was successfully deleted!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }
}
