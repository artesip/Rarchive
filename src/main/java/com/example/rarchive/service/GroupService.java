package com.example.rarchive.service;

import com.example.rarchive.entity.GroupEntity;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Optional;

public interface GroupService {

    Optional<GroupEntity> findGroupById(long id);

    Collection<GroupEntity> getCollectionGroups(String login);

    ResponseEntity<?> getGroups();

    ResponseEntity<?> getGroupById(long id);

    ResponseEntity<?> addGroup(GroupEntity group);

    ResponseEntity<String> updateGroup(GroupEntity group);

    ResponseEntity<String> deleteGroup(long id);

}
