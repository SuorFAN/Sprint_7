import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import api.Order;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListOrderTest {

    private Order order;
    private Faker faker;
    private int track;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> colour;

    @Before
    public void init() {
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
        colour = List.of(new String[]{faker.beer().style(), "test"});
        track = order.create(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colour).extract().path("track");

    }

    @Test
    @DisplayName("Количество заказов курьера")
    @Description("Проверка запроса получения количества заказов курьера")
    public void getListOrdersTest() {

        ValidatableResponse response = order.getList();

        assertThat("Не корректный статус код при получении списка заказов",
                response.extract().statusCode(), equalTo(HttpStatus.SC_OK));

        List<String> responseList = response.extract().path("orders");
        assertThat("Пустой список заказов",
                responseList.size() > 0);

        assertThat("У заказа отсутствует номер",
                track, isA(Integer.class));
    }

    @After
    public void clear() {
        order.cancel(track);
    }
}
