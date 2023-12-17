package api;

import configs.RestAssuredConf;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Order{
    private static final String PATH = "/api/v1/orders";
    private static final String CANCEL_PATH = "/cancel";

    @Step("Orders - Создание заказа, /api/v1/orders")
    public ValidatableResponse create(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        String body = "{\n" +
                "    \"firstName\": \"" + firstName + "\",\n" +
                "    \"lastName\": \"" + lastName + "\",\n" +
                "    \"address\": \"" + address + "\",\n" +
                "    \"metroStation\": " + metroStation + ",\n" +
                "    \"phone\": \"" + phone + "\",\n" +
                "    \"rentTime\": " + rentTime + ",\n" +
                "    \"deliveryDate\": \"" + deliveryDate + "\",\n" +
                "    \"comment\": \"" + comment + "\",\n" +
                "    \"color\": [\n" +
                "        \"" + String.join(",", color)+ "\"\n" +
                "    ]\n" +
                "}";
        return given()
                .spec(RestAssuredConf.requestSpecification())
                .and()
                .body(body)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Orders - Отменить заказ, /api/v1/orders/cancel")
    public ValidatableResponse cancel(int track) {
        String body = "{\n" +
                "    \"track\": " + track + "\n" +
                "}";

        return given()
                .spec(RestAssuredConf.requestSpecification())
                .and()
                .body(body)
                .when()
                .put(PATH+CANCEL_PATH)
                .then();
    }

    @Step("Orders - Получение списка заказов, /api/v1/orders")
    public ValidatableResponse getList() {
        return given()
                .spec(RestAssuredConf.requestSpecification())
                .get(PATH)
                .then();
    }
}
