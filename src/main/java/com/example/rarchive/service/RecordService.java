package com.example.rarchive.service;

import com.example.rarchive.entity.GroupEntity;
import com.example.rarchive.entity.RecordEntity;
import com.example.rarchive.entity.RecordInfoEntity;
import com.example.rarchive.entity.UserEntity;
import com.example.rarchive.model.RecordModel;
import com.example.rarchive.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;


@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    public void save(RecordEntity record) {
        recordRepository.save(record);
    }

    public Optional<RecordEntity> findById(long id) {
        return recordRepository.findById(id);
    }

    public Optional<RecordEntity> findByRecordInfoEntity(RecordInfoEntity recordInfo) {
        return recordRepository.findByRecordInfoEntity(recordInfo);
    }

    public Collection<RecordEntity> getCollectionRecords(String login) {

        ArrayList<GroupEntity> groups = (ArrayList<GroupEntity>) groupService.getCollectionGroups(login);

        Collection<RecordEntity> records = new ArrayList<>();
        for (var group : groups) {
            records.addAll(recordRepository.findAllByGroupEntity(group));
        }
        return records;
    }

    public ResponseEntity<?> getRecords() {
        try {
            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            return ResponseEntity.ok(getCollectionRecords(loginFromToken).stream().map(RecordModel::toModel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    public ResponseEntity<?> addRecord(long groupId, RecordEntity record) {
        try {

            Optional<GroupEntity> groupOptional = groupService.findGroupById(groupId);
            record.setGroupEntity(groupOptional.orElseThrow(
                    () -> new Exception("Cant find group to add record!\n")
            ));
            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            if (groupOptional.get().getUser().getLogin().equals(loginFromToken)) {
                save(record);
            } else {
                throw new Exception("You are trying to add record to another user!\n");
            }

            return ResponseEntity.ok("Record was saved!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    public ResponseEntity<String> updateRecord(RecordEntity record) {
        try {
            RecordEntity recordFromRepo = recordRepository.findById(record.getId()).orElseThrow(
                    () -> new Exception("Cant find record!\n")
            );

            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!record.equals(recordFromRepo) && recordFromRepo.getGroupEntity().getUser().getLogin().equals(loginFromToken)) {
                record.updateFields(recordFromRepo);
                save(record);
            } else {
                throw new Exception("You are trying to update record from another user!\n");
            }

            return ResponseEntity.ok("Record was successfully updated!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    public ResponseEntity<String> deleteRecord(long id) {
        try {
            RecordEntity record = recordRepository.findById(id).orElseThrow(
                    () -> new Exception("Cant find record with this id!\n")
            );

            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            if (record.getGroupEntity().getUser().getLogin().equals(loginFromToken)) {
                recordRepository.delete(record);
            } else {
                throw new Exception("You are trying to delete record from another user!\n");
            }

            if (recordRepository.findById(id).isEmpty()) {
                return ResponseEntity.ok("Record was successfully deleted!\n");
            }

            return ResponseEntity.badRequest().body("Something went wrong!\n");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

}
