package ru.yandex.praktikum.user;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.models.User;
import ru.yandex.praktikum.utils.Specification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserClient {
    private static final String USER_LOGIN_ENDPOINT = "api/auth/login";
    private static final String USER_REGISTER_ENDPOINT = "api/auth/register";
    private static final String USER_GET_ENDPOINT = "api/auth/user";
    private static final String USER_PATCH_ENDPOINT = "api/auth/user";
    private static final String USER_DELETE_ENDPOINT = "api/auth/user";

    @Step("Send post request to /api/auth/login")
    public Response loginUser(User user) {
        return given()
                .spec(Specification.requestSpecification())
                .body(user)
                .when()
                .post(USER_LOGIN_ENDPOINT);
    }

    @Step("Send post request to /api/auth/register")
    public Response registerUser(User user) {
        return given()
                .spec(Specification.requestSpecification())
                .body(user)
                .when()
                .post(USER_REGISTER_ENDPOINT);
    }

    @Step("Send get request to api/auth/user")
    public Response getUser(String bearerToken) {
        return given()
                .spec(Specification.requestSpecification())
                .header("Authorization", bearerToken)
                .when()
                .get(USER_GET_ENDPOINT);
    }

    @Step("Send patch request to api/auth/user")
    public Response patchUser(User user, String bearerToken) {
        return given()
                .spec(Specification.requestSpecification())
                .header("Authorization", bearerToken)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch(USER_PATCH_ENDPOINT);
    }

    @Step("Send delete request to api/auth/user")
    public Response deleteUser(String bearerToken) {
        return given()
                .spec(Specification.requestSpecification())
                .header("Authorization", bearerToken)
                .when()
                .delete(USER_DELETE_ENDPOINT);
    }
}
