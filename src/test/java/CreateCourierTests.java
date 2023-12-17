import api.Courier;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateCourierTests {

    private Courier courier;
    private Faker faker;
    private String login;
    private String password;
    private String firstName;

    @Before
    public void init() {
        courier = new Courier();
        faker=new Faker();
        login = faker.name().username();
        password = faker.crypto().sha256();
        firstName = faker.name().firstName();
    }

    @Test
    @DisplayName("Добавление курьера в систему")
    @Description("Проверка запроса добавления нового курьера")
    public void CourierCreateTest() {
        ValidatableResponse response = courier.create(login, password, firstName);

        assertEquals("Некорректный статус код при добавлении курьера",
                HttpStatus.SC_CREATED, response.extract().statusCode());

        assertEquals("Неверный ответ на запрос при добавлении курьера",
                true, response.extract().path("ok"));
    }

    @Test
    @DisplayName("Повторное добавление курьера")
    @Description("Проверка возможности добавления дубликата курьера")
    public void twoSameCourierCreateTest() {

        courier.create(login, password, firstName);
        ValidatableResponse responseSecond = courier.create(login, password, firstName);

        assertEquals("Не корректный статус код при добавлении дубликата курьера",
                HttpStatus.SC_CONFLICT, responseSecond.extract().statusCode());


        assertEquals("Ошибка в ответе при добавлении дубликата курьера",
                "Этот логин уже используется. Попробуйте другой.", responseSecond.extract().path("message"));

    }

    @After
    public void clear() {
        ValidatableResponse loginResponse = courier.login(login, password);
        if (courier.login(login, password).extract().statusCode() == HttpStatus.SC_OK){
            courier.delete(loginResponse.extract().path("id"));
        }
    }

}