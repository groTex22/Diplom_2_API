package site.nomoreparties.stellarburgers.orders;

import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.AbstractTestApi;
import site.nomoreparties.stellarburgers.clients.ClientOrder;
import site.nomoreparties.stellarburgers.clients.ClientUser;
import site.nomoreparties.stellarburgers.pojo.User;

@DisplayName("Тесты на получение заказов")
public class GetOrderUserTest {
    String accessToken;
    ClientUser clientUser;
    ClientOrder clientOrder;

    @Before
    public void beforeTest() {
        clientOrder = new ClientOrder();
        clientUser = new ClientUser();
        //Сразу создаем пользваотеля и получаем токен
        accessToken = clientUser.createUser(
                new User(AbstractTestApi.EMAIL, AbstractTestApi.PASSWORD, AbstractTestApi.NAME)
        ).extract().path("accessToken");
    }

    @After
    public void afterTest() {
        if (accessToken != null) {
            clientUser.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Получение заказа авторизованным пользователем")
    public void getOrderUserWithAuthTest() {
        clientOrder.getOrderUser(accessToken)
                .statusCode(HttpStatus.SC_OK)
                .and().body("success", Matchers.is(true))
                .and().body("orders", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Получение заказа НЕ авторизованным пользователем")
    public void getOrderUserNoAuthTest() {
        clientOrder.getOrderUser()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.equalTo("You should be authorised"));

    }
}
