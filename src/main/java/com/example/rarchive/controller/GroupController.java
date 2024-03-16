package com.example.rarchive.controller;

import com.example.rarchive.entity.GroupEntity;
import com.example.rarchive.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public ResponseEntity<?> getGroups() {
        return groupService.getGroups();
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
