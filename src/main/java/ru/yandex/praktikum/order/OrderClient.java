package ru.yandex.praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.models.Order;
import ru.yandex.praktikum.utils.Specification;

import static io.restassured.RestAssured.given;

public class OrderClient {
    public static final String ORDER_CREATE_ENDPOINT = "api/orders";
    public static final String ORDER_USER_ENDPOINT = "api/orders";
    public static final String ORDER_INGREDIENT_ENDPOINT = "api/ingredients";

    @Step("Send post request to api/orders")
    public Response createOrder(Order order, String bearerToken) {
        return given()
                .spec(Specification.requestSpecification())
                .headers("Authorization", bearerToken)
                .body(order)
                .when()
                .post(ORDER_CREATE_ENDPOINT);
    }

    @Step("Send get request to api/ingredients/")
    public static Response getIngredientsFromOrder() {
        return given()
                .spec(Specification.requestSpecification())
                .get(ORDER_INGREDIENT_ENDPOINT);
    }

    @Step("Send get request to api/orders")
    public static Response getOrderUser(String bearerToken) {
        return given()
                .spec(Specification.requestSpecification())
                .headers("Authorization", bearerToken)
                .get(ORDER_USER_ENDPOINT);
    }
}
