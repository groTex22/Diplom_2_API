package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.AbstractTestApi;
import site.nomoreparties.stellarburgers.clients.ClientUser;
import site.nomoreparties.stellarburgers.pojo.User;

/*Проверяем создание пользователя
* В тестах есть проблема, что в случае если они упадут*/
@DisplayName("тесты на создание пользователя")
public class CreateUserTest {
    private User user;
    private ClientUser clientUser;
    private String token;

    /*Переменные, чтобы не писать всегда одно и тоже
     *вынесены в абстрактный класс, так удобнее если что будет их менять
     *сразу на все тесты*/

    @Before
    public void beforeTest() {
        //Создали объект ClientUser
        clientUser = new ClientUser();
        //Создаем объект для дальнейшей передачи в качестве JSON на эндпоинт
        user = new User();
    }

    @After
    public void aftetTest() {
        //Если токен пустой, то и удалять нечего
        if (token != null) {
            //Обвернем в блок исключений, в случае если в тесте курьера создать не удалось.
            //Удаляем и проверяем, что уделаение успешное.
            Response response = clientUser.deleteUser(token);
            Assert.assertTrue("не удалось удалить"
                    , response.path("success"));
        }
    }

    @Test
    public void createUserFieldAllParamTest() {
        //Заполним все параметры
        user.setEmail(AbstractTestApi.EMAIL);
        user.setPassword(AbstractTestApi.PASSWORD);
        user.setName(AbstractTestApi.NAME);

        ValidatableResponse response = clientUser.createUser(user);
        //При успешной регистрации запоминаем токен, для дальнейшего удаления
        token = response.extract().path("accessToken");
        Assert.assertNotNull(token);

        response.statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createUserIncorrectEmailTest() {
        //Заполним все параметры
        user.setEmail("test_Ryzkov");
        user.setPassword(AbstractTestApi.PASSWORD);
        user.setName(AbstractTestApi.NAME);


        ValidatableResponse response = clientUser.createUser(user);
        response.statusCode( HttpStatus.SC_INTERNAL_SERVER_ERROR);
        /*Assert.assertEquals("Ожидаем ошибку сервера",
                response.extract().statusCode(), HttpStatus.SC_INTERNAL_SERVER_ERROR);*/
    }

    @Test
    public void createUserNoEmailTest() {
        //Заполним все параметры кроме мыла
        user.setPassword(AbstractTestApi.PASSWORD);
        user.setName(AbstractTestApi.NAME);

        ValidatableResponse response = clientUser.createUser(user);
        //И проверки
        response.statusCode(HttpStatus.SC_FORBIDDEN)
                .and().body("message"
                        , Matchers.equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserNoPasswordTest() {
        //Заполним все параметры кроме пароля
        user.setEmail(AbstractTestApi.EMAIL);
        user.setName(AbstractTestApi.NAME);

        ValidatableResponse response = clientUser.createUser(user);
        //И проверки
        response.statusCode(HttpStatus.SC_FORBIDDEN)
                .and().body("message"
                        , Matchers.equalTo("Email, password and name are required fields"));

    }

    @Test
    public void createUserNoNameTest() {
        //Заполним все параметры кроме имени
        user.setEmail(AbstractTestApi.EMAIL);
        user.setPassword(AbstractTestApi.PASSWORD);

        ValidatableResponse response = clientUser.createUser(user);

        response.statusCode(HttpStatus.SC_FORBIDDEN)
                .and().body("message"
                        , Matchers.equalTo("Email, password and name are required fields"));

    }


    //Повторная регистрация
    @Test
    public void createUserDoubleTest() {
        //Заполним все параметры кроме имени
        user.setEmail(AbstractTestApi.EMAIL);
        user.setPassword(AbstractTestApi.PASSWORD);
        user.setName(AbstractTestApi.NAME);

        //Регистрируемся в первый раз
        ValidatableResponse response = clientUser.createUser(user);
        //При успешной регистрации запоминаем токен, для дальнейшего удаления не должен быть null
        token = response.extract().path("accessToken");
        Assert.assertNotNull(token);
        //проверяем статус Нужен успех = 200
        response.statusCode(HttpStatus.SC_OK);
        //Регистрируемся второй раз
        clientUser.createUser(user).statusCode(HttpStatus.SC_FORBIDDEN);
    }


}
