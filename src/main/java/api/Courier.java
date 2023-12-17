package api;

import configs.RestAssuredConf;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class Courier{
    private static final String PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/login";

    @Step("Courier - Создание курьера, /api/v1/courier")
    public ValidatableResponse create(String login, String password, String firstName) {
        String body = "{\n" +
                "    \"login\": \"" + login + "\",\n" +
                "    \"password\": \"" + password + "\",\n" +
                "    \"firstName\": \"" + firstName + "\"\n" +
                "}";
        return given()
                .spec(RestAssuredConf.requestSpecification())
                .and()
                .body(body)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Courier - Логин курьера в системе, /api/v1/courier/login")
    public ValidatableResponse login(String login, String password) {
        String body = "{\n" +
                "    \"login\": \"" + login + "\",\n" +
                "    \"password\": \"" + password + "\"\n" +
                "}";
        return given()
                .spec(RestAssuredConf.requestSpecification())
                .and()
                .body(body)
                .when()
                .post(PATH+LOGIN_PATH)
                .then();
    }

    @Step("Courier - Удаление курьера, /api/v1/courier/:id")
    public ValidatableResponse delete(int id) {
        String body = "{\n" +
                "    \"id\": \"" + id + "\"\n" +
                "}";
        return given()
                .spec(RestAssuredConf.requestSpecification())
                .and()
                .body(body)
                .when()
                .delete(PATH+"/"+id)
                .then();
    }
}
