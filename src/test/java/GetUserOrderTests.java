import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.models.Order;
import ru.yandex.praktikum.models.User;
import ru.yandex.praktikum.order.OrderClient;
import ru.yandex.praktikum.user.UserClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.order.OrderGenerator.getListOrder;
import static ru.yandex.praktikum.user.UserGenerator.getRandomUser;

public class GetUserOrderTests {
    private UserClient userClient;
    private User user;
    private OrderClient orderClient;
    private Order order;
    private String bearerToken;
    private Response responseRegister;

    @Before
    public void setUp() {
        user = getRandomUser();
        userClient = new UserClient();
        order = getListOrder();
        orderClient = new OrderClient();
        responseRegister = userClient.registerUser(user);
        bearerToken = responseRegister.path("accessToken");
    }

    @Test
    @Epic(value = "Order's test")
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Проверка получения заказов авторизованного пользователя")
    public void getOrderUserWithAuthorization() {
        Response responseGetOrderUser = orderClient.getOrderUser(bearerToken);

        assertEquals("Статус код неверный", SC_OK, responseGetOrderUser.statusCode());
        assertEquals(true, responseGetOrderUser.path("success"));
    }

    @Test
    @Epic(value = "Order's test")
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Проверка получения заказов неавторизованного пользователя")
    public void getOrderUserWithoutAuthorization() {
        bearerToken = "";
        Response responseGetOrderUser = orderClient.getOrderUser(bearerToken);

        assertEquals("Статус код неверный", SC_UNAUTHORIZED, responseGetOrderUser.statusCode());
        assertEquals("You should be authorised", responseGetOrderUser.path("message"));
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
