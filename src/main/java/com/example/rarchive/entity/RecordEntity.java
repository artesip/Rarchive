package com.example.rarchive.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "record")
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "status")
    private String status;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity groupEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "record_info_id", referencedColumnName = "id")
    private RecordInfoEntity recordInfoEntity;

    public RecordEntity() {
    }

    public void updateFields(RecordEntity fromRecord) {
        this.setGroupEntity(fromRecord.getGroupEntity());
        this.setRecordInfoEntity(fromRecord.getRecordInfoEntity());
    }

    public String getLogin() {
        return groupEntity.getLogin();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordEntity record = (RecordEntity) o;
        return Objects.equals(status, record.status) && Objects.equals(title, record.title) && Objects.equals(date, record.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, title, date);
    }
}
