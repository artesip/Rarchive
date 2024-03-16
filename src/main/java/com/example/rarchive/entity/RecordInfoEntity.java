package com.example.rarchive.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "record_info")
public class RecordInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_description")
    private String description;

    @Column(name = "rating")
    private int rating;

    @Column(name = "status")
    private String status;

    @Column(name = "link")
    private String link;

    @OneToOne(mappedBy = "recordInfoEntity")
    private RecordEntity recordEntity;

    public RecordInfoEntity(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordInfoEntity that = (RecordInfoEntity) o;
        return rating == that.rating && Objects.equals(description, that.description) && Objects.equals(status, that.status) && Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, rating, status, link);
    }
}
