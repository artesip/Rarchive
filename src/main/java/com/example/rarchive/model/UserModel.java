package com.example.rarchive.model;

import com.example.rarchive.entity.GroupEntity;
import com.example.rarchive.entity.UserEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class UserModel {
    private String login;
    private String nickname;
    public static UserModel toModel(UserEntity user){
        UserModel userModel = new UserModel();

        userModel.setLogin(user.getLogin());
        userModel.setNickname(user.getNickname());

        return userModel;
    }

}
