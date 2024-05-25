package com.example.rarchive.service.impl;

import com.example.rarchive.entity.GroupEntity;
import com.example.rarchive.entity.RecordEntity;
import com.example.rarchive.exception.NoSuchDataException;
import com.example.rarchive.exception.UnauthorizedAccessException;
import com.example.rarchive.model.RecordModel;
import com.example.rarchive.repository.RecordRepository;
import com.example.rarchive.service.GroupService;
import com.example.rarchive.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    private final GroupService groupService;

    @Autowired
    RecordServiceImpl(RecordRepository recordRepository, GroupService groupService) {
        this.groupService = groupService;
        this.recordRepository = recordRepository;
    }

    public void save(RecordEntity record) {
        recordRepository.save(record);
    }

    public Optional<RecordEntity> findById(long id) {
        return recordRepository.findById(id);
    }

    public List<RecordEntity> getListRecords(String login) {

        ArrayList<GroupEntity> groups = (ArrayList<GroupEntity>) groupService.getCollectionGroups(login);

        return groups.stream().collect(
                ArrayList::new,
                (list, group) -> list.addAll(recordRepository.findAllByGroupEntity(group)),
                ArrayList::addAll
        );
    }

    @Override
    public ResponseEntity<?> getRecords() {
        try {
            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            return ResponseEntity.ok(getListRecords(loginFromToken).stream().map(RecordModel::toModel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getRecordById(long id) {
        try {
            return ResponseEntity.ok(recordRepository.findById(id).orElseThrow(NoSuchDataException::new));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addRecord(long groupId, RecordEntity record) {
        try {

            GroupEntity group = groupService.findGroupById(groupId).orElseThrow(
                    () -> new NoSuchDataException("Cant find group to add record!\n")
            );
            record.setGroupEntity(group);
            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            if (!record.getLogin().equals(loginFromToken)) {
                throw new UnauthorizedAccessException("You are trying to add record to another user!\n");
            }
            save(record);

            return ResponseEntity.ok("Record was saved!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> updateRecord(RecordEntity record) {
        try {
            RecordEntity recordFromRepo = recordRepository.findById(record.getId()).orElseThrow(
                    () -> new NoSuchDataException("Cant find record!\n")
            );

            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            if (!recordFromRepo.getLogin().equals(loginFromToken)) {
                throw new UnauthorizedAccessException("You are trying to update record from another user!\n");
            }
            record.updateFields(recordFromRepo);
            save(record);

            return ResponseEntity.ok("Record was successfully updated!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteRecord(long id) {
        try {
            RecordEntity record = recordRepository.findById(id).orElseThrow(
                    () -> new NoSuchDataException("Cant find record with this id!\n")
            );

            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            if (!record.getGroupEntity().getUser().getLogin().equals(loginFromToken)) {
                throw new UnauthorizedAccessException("You are trying to delete record from another user!\n");
            }
            recordRepository.delete(record);

            if (recordRepository.findById(id).isPresent()) {
                return ResponseEntity.badRequest().body("Something went wrong!\n");
            }

            return ResponseEntity.ok("Record was successfully deleted!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

}
