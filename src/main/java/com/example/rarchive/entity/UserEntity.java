package com.example.rarchive.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<GroupEntity> groupEntityList;

    public UserEntity() {}

    public void updateFields(UserEntity anotherUser) {
        if (anotherUser.getPassword() != null && !anotherUser.getPassword().isEmpty()) {
            this.password = anotherUser.getPassword();
        }
        if (anotherUser.getNickname() != null && !anotherUser.getNickname().isEmpty()) {
            this.nickname = anotherUser.getNickname();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, nickname);
    }
}
