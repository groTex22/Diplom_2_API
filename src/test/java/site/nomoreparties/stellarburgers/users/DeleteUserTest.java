package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.AbstractTestApi;
import site.nomoreparties.stellarburgers.clients.ClientUser;
import site.nomoreparties.stellarburgers.pojo.User;

//Отдельно проверяем удаление, чтобы по упавшим тестом было понятно
//если упал именно он
@DisplayName("тесты на удаление пользователя")
public class DeleteUserTest {
    ClientUser clientUser;
    User user;

    @Before
    public void beforeTest() {
        //Создали объект ClientUser
        clientUser = new ClientUser();
        //Создади JSON для создание пользователя сразу с параметрами
        user = new User(AbstractTestApi.EMAIL
                , AbstractTestApi.PASSWORD
                , AbstractTestApi.NAME);
    }

    @Test
    public void deleteUserSuccess() {
        //Создание проверяется в другом классе, здесь просто создаем
        ValidatableResponse response = clientUser.createUser(user);
        String token = response.extract().path("accessToken");

        Response deleteResponse = clientUser.deleteUser(token);
        //Проверки
        deleteResponse.then().statusCode(HttpStatus.SC_ACCEPTED)
                        .and()
                        .body("success", Matchers.is(true))
                        .and()
                        .body("message", Matchers.equalTo("User successfully removed"));
    }


}
