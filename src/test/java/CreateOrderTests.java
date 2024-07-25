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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.order.OrderGenerator.getListOrder;
import static ru.yandex.praktikum.user.UserGenerator.getRandomUser;

public class CreateOrderTests {
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
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка создания заказа с авторизацией")
    public void createOrderWithAuthorization() {
        Response responseCreateOrder = orderClient.createOrder(order, bearerToken);

        assertEquals("Статус код неверный", SC_OK, responseCreateOrder.statusCode());
        assertEquals(true, responseCreateOrder.path("success"));
    }

    @Test
    @Epic(value = "Order's test")
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка создания заказа без авторизации")
    public void createOrderWithoutAuthorization() {
        bearerToken = "";
        Response responseCreateOrder = orderClient.createOrder(order, bearerToken);

        assertEquals("Статус код неверный", SC_OK, responseCreateOrder.statusCode());
        assertEquals(true, responseCreateOrder.path("success"));
    }

    @Test
    @Epic(value = "Order's test")
    @DisplayName("Создание заказа без ингридиентов")
    @Description("Проверка создания заказа без ингридиентов")
    public void createOrderWithoutIngredient() {
        List<String> emptyList = Collections.emptyList();

        order.setIngredients(emptyList);

        Response responseCreateOrder = orderClient.createOrder(order, bearerToken);

        assertEquals("Статус код неверный", SC_BAD_REQUEST, responseCreateOrder.statusCode());
        assertEquals("Ingredient ids must be provided", responseCreateOrder.path("message"));
    }

    @Test
    @Epic(value = "Order's test")
    @DisplayName("Создание заказа с неверным хешем")
    @Description("Проверка создания заказа с неверным хешем")
    public void createOrderWithWrongIngredient() {
        List<String> wrongIngredient = new ArrayList<>();

        wrongIngredient.add("111116e4dc916e00276b2811");

        order.setIngredients(wrongIngredient);

        Response responseCreateOrder = orderClient.createOrder(order, bearerToken);

        assertEquals("Статус код неверный", SC_BAD_REQUEST, responseCreateOrder.statusCode());
        assertEquals("One or more ids provided are incorrect", responseCreateOrder.path("message"));
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
