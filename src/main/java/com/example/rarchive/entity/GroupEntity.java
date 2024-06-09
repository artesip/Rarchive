package com.example.rarchive.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "user_groups")
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupEntity")
    private List<RecordEntity> recordEntityList;

    public GroupEntity() {
    }

    public String getLogin() {
        return user.getLogin();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupEntity group = (GroupEntity) o;
        return Objects.equals(title, group.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    public void updateFields(GroupEntity fromGroup) {
        this.setUser(fromGroup.getUser());
        this.setRecordEntityList(fromGroup.getRecordEntityList());
    }
}