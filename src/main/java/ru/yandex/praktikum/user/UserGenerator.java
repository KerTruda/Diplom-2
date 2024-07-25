package ru.yandex.praktikum.user;

import ru.yandex.praktikum.models.User;

import static ru.yandex.praktikum.utils.Utils.randomEmail;
import static ru.yandex.praktikum.utils.Utils.randomString;

public class UserGenerator {
    public static User getRandomUser() {

        return new User()
                .setEmail(randomEmail())
                .setPassword(randomString(10))
                .setName(randomString(10));

    }
}
