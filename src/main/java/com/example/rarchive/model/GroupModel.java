package com.example.rarchive.model;

import com.example.rarchive.entity.GroupEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GroupModel implements Serializable {
    private long id;

    private String title;

    private List<RecordModel> recordModels;

    public static GroupModel toModel(GroupEntity group) {
        GroupModel temp = new GroupModel();
        temp.setId(group.getId());
        temp.setTitle(group.getTitle());
        temp.setRecordModels(group.getRecordEntityList().stream().map(RecordModel::toModel).collect(Collectors.toList()));
        return temp;
    }
}
