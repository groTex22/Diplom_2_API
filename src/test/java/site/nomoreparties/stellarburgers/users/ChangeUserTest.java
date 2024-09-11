package site.nomoreparties.stellarburgers.users;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.AbstractTestApi;
import site.nomoreparties.stellarburgers.clients.ClientUser;
import site.nomoreparties.stellarburgers.pojo.User;

import static org.hamcrest.Matchers.equalTo;

@DisplayName("Изменение и получение данных пользователя")
public class ChangeUserTest {
    private ClientUser clientUser;
    private User user;
    String accessToken;

    @Before
    public void beforeTest() {
        clientUser = new ClientUser();
        user = new User(AbstractTestApi.EMAIL, AbstractTestApi.PASSWORD, AbstractTestApi.NAME);
        //Зарегистрируем пользователя и получим токен
        accessToken = clientUser.createUser(user).extract().path("accessToken");
        Assert.assertNotNull(accessToken);
    }

    @After
    public void afterTest() {
        //Удаляемся
        clientUser.deleteUser(accessToken);
    }
    /*Проверяем получение информации о пользователи*/
    @Test
    @DisplayName("Получение информации о пользователи")
    public void getDataUserTest() {
        Response getResponce = clientUser.getDataUser(accessToken);
        //Проверки
        getResponce.then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and().body("user.email" , equalTo(AbstractTestApi.EMAIL ))
                .and().body("user.name" , equalTo(AbstractTestApi.NAME));
    }

    /*Проверяем изменение имени пользователя*/
    @Test
    @DisplayName("Изменение имени пользователя")
    public void changeNameWithAuthTest() {
        String newName = AbstractTestApi.NAME + "11";
        user.setName(newName);

        Response changeResponce = clientUser.changeDataUser(accessToken, user);
        //Проверки
        changeResponce.then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and().body("user.email" , equalTo(AbstractTestApi.EMAIL ))
                .and().body("user.name" , equalTo(newName));
    }

    /*Проверяем изменение пароля пользователя*/
    @Test
    @DisplayName("Изменение пароля пользователя")
    public void changePasswordWithAuthTest() {
        user.setPassword(AbstractTestApi.PASSWORD + "111");

        Response changeResponce = clientUser.changeDataUser(accessToken, user);
        //Проверки
        //Пароль не возвращается в ответе, поэтому просто проверим, что ответ он не пустой
        changeResponce.then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and().body("user" , Matchers.notNullValue());
    }

    //Проверяем изменение email пользователя*/
    @Test
    @DisplayName("Изменение email пользователя")
    public void changeEmaildWithAuthTest() {
        String newEmail = AbstractTestApi.EMAIL + "11";
        user.setEmail(newEmail);

        Response changeResponce = clientUser.changeDataUser(accessToken, user);
        //Проверки можно просто assertEquals, но так лаконичнее
        changeResponce.then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and().body("user.email" , equalTo(newEmail))
                .and().body("user.name" , equalTo(AbstractTestApi.NAME));
    }

    /*Проверяем изменение имени без авторизации
    * ожидание ошибка, что не авторизованы
    * Досточно одного сценария, падаем при любом body*/
    @Test
    @DisplayName("Изменение без авторизации")
    public void changeNameNoAuthTest() {

        user.setName(AbstractTestApi.NAME + "11");
        //Вызовим и сразу проверим код, здесь этого достаточно
        clientUser.changeDataUser(user).then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
