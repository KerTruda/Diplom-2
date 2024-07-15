package ru.yandex.praktikum.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreds {
    private String email;
    private String password;
    private String name;

    public static UserCreds credsFromUser(User user){
        return new UserCreds(user.getEmail(), user.getPassword(), user.getName());
    }
}
