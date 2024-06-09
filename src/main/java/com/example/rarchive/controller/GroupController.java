package com.example.rarchive.controller;

import com.example.rarchive.entity.GroupEntity;
import com.example.rarchive.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/group")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<?> getGroups() {
        return groupService.getGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupsById(@PathVariable long id) {
        return groupService.getGroupById(id);
    }

    @PostMapping
    public ResponseEntity<?> addGroup(@RequestBody GroupEntity group) {
        return groupService.addGroup(group);
    }

    @PutMapping
    public ResponseEntity<String> updateGroup(@RequestBody GroupEntity group) {
        return groupService.updateGroup(group);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteGroup(@RequestParam(name = "id") long id) {
        return groupService.deleteGroup(id);
    }

}
