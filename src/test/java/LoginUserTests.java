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
import static ru.yandex.praktikum.utils.Utils.randomEmail;
import static ru.yandex.praktikum.utils.Utils.randomString;

public class LoginUserTests {
    private UserClient userClient;
    private User user;
    private String bearerToken;
    private Response responseRegister;

    @Before
    public void setUp() {
        user = getRandomUser();
        userClient = new UserClient();
        responseRegister = userClient.registerUser(user);
    }

    @Test
    @Epic(value = "User's test")
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка авторизации под существующим пользователем")
    public void loginUser() {
        Response responseLogin = userClient.loginUser(user);
        assertEquals("Статус код неверный", SC_OK, responseLogin.statusCode());
        assertEquals(true, responseLogin.path("success"));
    }

    @Test
    @Epic(value = "User's test")
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка авторизации с неверным паролем")
    public void loginUserWithWrongPassword() {
        user.setPassword(randomString(12));
        Response responseLogin = userClient.loginUser(user);
        assertEquals("Статус код неверный", SC_UNAUTHORIZED, responseLogin.statusCode());
        assertEquals("email or password are incorrect", responseLogin.path("message"));
    }

    @Test
    @Epic(value = "User's test")
    @DisplayName("Вход с неверным email")
    @Description("Проверка авторизации с неверным email")
    public void loginUserWithWrongEmail() {
        user.setEmail(randomEmail());
        Response responseLogin = userClient.loginUser(user);
        assertEquals("Статус код неверный", SC_UNAUTHORIZED, responseLogin.statusCode());
        assertEquals("email or password are incorrect", responseLogin.path("message"));
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
