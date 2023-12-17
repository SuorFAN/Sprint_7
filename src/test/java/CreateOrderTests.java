import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import api.Order;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.text.ParseException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class CreateOrderTests {

    private Order order;
    Faker faker;
    private int track;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private final List<String> colour;

    @Before
    public void init() throws ParseException {
        order = new Order();
        faker = new Faker();
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        address = faker.address().fullAddress();
        metroStation = faker.code().imei();
        phone = faker.phoneNumber().phoneNumber();
        rentTime = 5;
        deliveryDate = "2023-01-01";
        comment = faker.chuckNorris().fact();
    }

    public CreateOrderTests(List<String> colour) {
        this.colour = colour;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("GREY", "BLACK")},
                {List.of()},
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Запрос создания заказа c выбором цвета")
    public void createOrderTest() {
        ValidatableResponse response = order.create(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colour);
        System.out.println(response.extract().asPrettyString());
        track = response.extract().path("track");

        assertThat("Не корректный статус код при создании заказа c выбором цвета",
                response.extract().statusCode(), equalTo(HttpStatus.SC_CREATED));

        assertThat("Ошибка трек номера при создании заказа c выбором цвета",
                response.extract().path("track"), instanceOf(Integer.class));
    }

    @After
    public void clear() {
            order.cancel(track);
    }
}
