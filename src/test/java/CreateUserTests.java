import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.models.User;
import ru.yandex.praktikum.user.UserClient;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.user.UserGenerator.getRandomUser;

public class CreateUserTests {
    private UserClient userClient;
    private User user;
    private String bearerToken;

    @Before
    public void setUp() {
        user = getRandomUser();
        userClient = new UserClient();
    }

    @Test
    @Epic(value = "User's test")
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка создание уникального пользователя")
    public void createUser() {
        Response response = userClient.registerUser(user);
        bearerToken = response.path("accessToken");
        assertEquals("Статус код неверный", SC_OK, response.statusCode());
        assertEquals(true, response.path("success"));
    }

    @Test
    @Epic(value = "User's test")
    @DisplayName("Создание существующего пользователя")
    @Description("Проверка создания существующего пользователя")
    public void createAlreadyExistsUser() {
        Response responseFirst = userClient.registerUser(user);
        bearerToken = responseFirst.path("accessToken");
        Response responseSecond = userClient.registerUser(user);
        assertEquals("Статус код неверный", SC_FORBIDDEN, responseSecond.statusCode());
        assertEquals("User already exists", responseSecond.path("message"));
    }

    @Test
    @Epic(value = "User's test")
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка пользователя без имени")
    public void createUserWithoutName() {
        user.setName("");
        Response response = userClient.registerUser(user);
        bearerToken = response.path("accessToken");
        assertEquals("Статус код неверный", SC_FORBIDDEN, response.statusCode());
        assertEquals("Email, password and name are required fields", response.path("message"));
    }

    @Test
    @Epic(value = "User's test")
    @DisplayName("Создание пользователя без email")
    @Description("Проверка пользователя без email")
    public void createUserWithoutEmail() {
        user.setEmail("");
        Response response = userClient.registerUser(user);
        bearerToken = response.path("accessToken");
        assertEquals("Статус код неверный", SC_FORBIDDEN, response.statusCode());
        assertEquals("Email, password and name are required fields", response.path("message"));
    }

    @Test
    @Epic(value = "User's test")
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка пользователя без пароля")
    public void createUserWithoutPassword() {
        user.setPassword("");
        Response response = userClient.registerUser(user);
        bearerToken = response.path("accessToken");
        assertEquals("Статус код неверный", SC_FORBIDDEN, response.statusCode());
        assertEquals("Email, password and name are required fields", response.path("message"));
    }

    @After
    public void tearDown(){
        Response response = userClient.loginUser(user);
        if (response.statusCode() == SC_OK){
            bearerToken = response.path("accessToken");
            assertEquals("User successfully removed", userClient.deleteUser(bearerToken).path("message"));
        }
    }
}
