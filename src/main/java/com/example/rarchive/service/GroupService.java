package com.example.rarchive.service;

import com.example.rarchive.entity.GroupEntity;
import com.example.rarchive.exception.NoSuchDataException;
import com.example.rarchive.exception.UnauthorizedAccessException;
import com.example.rarchive.model.GroupModel;
import com.example.rarchive.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    public Optional<GroupEntity> findGroupById(long id) {
        return groupRepository.findById(id);
    }

    public void save(GroupEntity group) {
        groupRepository.save(group);
    }

    public Collection<GroupEntity> getCollectionGroups(String login) {

        if (login == null || login.isEmpty()) {
            throw new NoSuchDataException("Empty Login!\n");
        }

        return groupRepository.findAllByUserId(userService.getUserIdByLogin(login));
    }

    public ResponseEntity<?> getGroups() {
        try {
            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            List<GroupEntity> groupEntityList = (ArrayList<GroupEntity>) getCollectionGroups(loginFromToken);

            return ResponseEntity.ok(groupEntityList.stream().map(GroupModel::toModel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    public ResponseEntity<?> addGroup(GroupEntity group) {
        try {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();

            group.setUser(userService.findByLogin(login).orElseThrow(
                    () -> new NoSuchDataException("""
                            Cant find user!
                            Empty login or dead token or no such user!
                            """)
            ));
            save(group);

            return ResponseEntity.ok("Group was successfully saved!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    public ResponseEntity<String> updateGroup(GroupEntity group) {
        try {
            GroupEntity groupFromRepo = groupRepository.findById(group.getId()).orElseThrow(
                    () -> new NoSuchDataException("Cant find group with such id!\n")
            );

            String loginFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

            if (!groupFromRepo.getLogin().equals(loginFromToken)) {
                throw new UnauthorizedAccessException();
            }

            group.updateFields(groupFromRepo);
            save(group);

            return ResponseEntity.ok("Group was successfully updated!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    public ResponseEntity<String> deleteGroup(long id) {
        try {
            String tokenLogin = SecurityContextHolder.getContext().getAuthentication().getName();
            GroupEntity group = groupRepository.findById(id).orElseThrow(
                    () -> new NoSuchDataException("No such group for this id!\n")
            );

            if (!group.getLogin().equals(tokenLogin)) {
                throw new UnauthorizedAccessException();
            }

            groupRepository.delete(group);

            if (groupRepository.findById(id).isPresent()) {
                return ResponseEntity.badRequest().body("Something went wrong!\n");
            }

            return ResponseEntity.ok("Group was successfully deleted!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

}
