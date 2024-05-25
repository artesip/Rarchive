package com.example.rarchive.model;

import com.example.rarchive.entity.UserEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@RedisHash(timeToLive = 60)
public class UserModel implements Serializable {
    private String login;
    private String nickname;
    public static UserModel toModel(UserEntity user){
        UserModel userModel = new UserModel();
        userModel.setLogin(user.getLogin());
        userModel.setNickname(user.getNickname());

        return userModel;
    }

}
