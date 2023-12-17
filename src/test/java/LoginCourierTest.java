import api.Courier;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoginCourierTest {
    private Courier courier;
    private Faker faker;
    private String login;
    private String password;

    @Before
    public void init() {
        courier = new Courier();
        faker=new Faker();
        login = faker.name().username();
        password = faker.crypto().sha256();
        String firstName = faker.name().firstName();

        courier.create(login, password, firstName);
    }

    @Test
    @DisplayName("Логин курьера")
    @Description("Проверка запроса с существующей парой логин-пароль")
    public void loginCourierTest() {

        ValidatableResponse Response = courier.login(login, password);

        assertThat("Не корректный статус код при авторизации курьера",
                Response.extract().statusCode(), equalTo(HttpStatus.SC_OK));

        assertThat("Ошибка в ответе при авторизации курьера",
                Response.extract().path("id"), instanceOf(Integer.class));

    }

    @Test
    @DisplayName("Проверка логина")
    @Description("Проверка запроса с несуществующим логином")
    public void loginCourierWithWrongLoginTest() {
        String login = "wrong";
        ValidatableResponse Response = courier.login(login, password);

        assertThat("Не корректный статус код при авторизации курьера с несуществующим логином",
                Response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Ошибка в ответе при авторизации курьера с несуществующим логином",
                Response.extract().path("message"), equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверка пароля не корректного пароля")
    @Description("Проверка запроса авторизации с не корректным паролем")
    public void loginCourierWithWrongPasswordTest() {
        String password = "wrong";

        ValidatableResponse Response = courier.login(login, password);

        assertThat("Не верный статус кода при авторизации курьера с не корректным паролем",
                Response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Ошибка в ответе при авторизации курьера с не корректным паролем",
                Response.extract().path("message"), equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверка отсутствия пароля")
    @Description("Проверка запроса авторизации без пароля")
    public void loginCourierWithoutPasswordTest() {
        ValidatableResponse Response = courier.login(login, "");

        assertThat("Не корректный статус код при авторизации курьера без пароля",
                Response.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));

        assertThat("Ошибка в ответе при авторизации без пароля",
                Response.extract().path("message"), equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка отсутствия логина")
    @Description("Проверка запроса авторизации без логина")
    public void loginCourierWithoutLoginTest() {
        ValidatableResponse Response = courier.login("", password);

        assertThat("Не корректный статус код при авторизации курьера без логина",
                Response.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));

        assertThat("Ошибка в ответе при авторизации без логина",
                Response.extract().path("message"), equalTo("Недостаточно данных для входа"));
    }


    @After
    public void clear() {
        ValidatableResponse loginResponse = courier.login(login, password);
        if (courier.login(login, password).extract().statusCode() == HttpStatus.SC_OK) {
            courier.delete(loginResponse.extract().path("id"));
        }
    }

}
