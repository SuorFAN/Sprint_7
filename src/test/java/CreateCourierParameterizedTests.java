import api.Courier;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateCourierParameterizedTests {

    private Courier courier;
    private static Faker faker=new Faker();
    private String login;
    private String password;
    private String firstName;

    @Before
    public void init() {
        courier = new Courier();
    }

    public CreateCourierParameterizedTests(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {"", faker.crypto().sha256(), faker.name().firstName()},
                {faker.name().username(), "", faker.name().firstName()},
        };
    }

    @Test
    @DisplayName("Добавление курьера с отсутствием данных ")
    @Description("Добавление курьера без необходимых данных в запросе")
    public void createCourierNegativeDataTest() {
        ValidatableResponse response = courier.create(login, password, firstName);

        assertEquals("Не корректный статус код при отсутствии необходимых данных",
                HttpStatus.SC_BAD_REQUEST, response.extract().statusCode());

        assertEquals("Ошибка в ответе при отсутствии необходимых данных",
                "Недостаточно данных для создания учетной записи", response.extract().path("message"));
    }

    @After
    public void clear() {
        ValidatableResponse loginResponse = courier.login(login, password);
        if (courier.login(login, password).extract().statusCode() == HttpStatus.SC_OK) {
            courier.delete(loginResponse.extract().path("id"));
        }
    }
}
