import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.models.User;
import ru.yandex.praktikum.user.UserClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.user.UserGenerator.getRandomUser;

public class ChangesUserTests {
    private UserClient userClient;
    private User user;
    private String bearerToken;
    private Response responseRegister;

    @Before
    public void setUp() {
        user = getRandomUser();
        userClient = new UserClient();
        responseRegister = userClient.registerUser(user);
        bearerToken = responseRegister.path("accessToken");
    }

    @Test
    @Epic(value = "User's test")
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Проверка изменения данных существующего пользователя с авторизацией")
    public void changeUserWithAuthorization(){
        User userChange = getRandomUser();
        Response responsePatch = userClient.patchUser(userChange, bearerToken);
        assertEquals("Статус код неверный", SC_OK, responsePatch.statusCode());
        assertEquals(true, responsePatch.path("success"));
    }

    @Test
    @Epic(value = "User's test")
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Проверка изменения данных существующего пользователя без авторизации")
    public void changeUserWithoutAuthorization(){
        User userChange = getRandomUser();
        bearerToken = "";
        Response responsePatch = userClient.patchUser(userChange, bearerToken);
        assertEquals("Статус код неверный", SC_UNAUTHORIZED, responsePatch.statusCode());
        assertEquals("You should be authorised", responsePatch.path("message"));
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
