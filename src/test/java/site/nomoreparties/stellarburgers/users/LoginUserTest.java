package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.*;
import site.nomoreparties.stellarburgers.AbstractTestApi;
import site.nomoreparties.stellarburgers.clients.ClientUser;
import site.nomoreparties.stellarburgers.pojo.LoginUser;
import site.nomoreparties.stellarburgers.pojo.User;

/*Проверяем авторизацию пользователя*/
@DisplayName("тесты на авторизацию пользователя")
public class LoginUserTest {
    private LoginUser loginUser;
    private static ClientUser clientUser;
    private static String token;

    /*Переменные, чтобы не писать всегда одно и тоже
     *вынесены в абстрактный класс, так удобнее если что будет их менять
     *сразу на все тесты*/


    /*Клиент и пользователя можно создать один раз для всего класса*/
    @BeforeClass
    public static void beforeClassTest() {
        //Создали объект clientCourier
        clientUser = new ClientUser();
        //Создаем юзера над которым будут проверки
        ValidatableResponse response = clientUser.createUser(
                        new User(AbstractTestApi.EMAIL
                                , AbstractTestApi.PASSWORD
                                , AbstractTestApi.NAME))
                .statusCode(HttpStatus.SC_OK);

        //Запомним токен, чтобы потом самоудалиться
        token = response.extract().path("accessToken");
    }

    /*А вот JSON для логина в каждый тест свой иначе его надо будет занулять*/
    @Before
    public void beforeTest() {
        //Создаем объект для дальнейшей передачи в качестве JSON на эндпоинт
        loginUser = new LoginUser();
    }

    @AfterClass
    public static void afterClassTest() {
        //Самоудалимя
        //Response response = clientUser.deleteUser(token);
        Assert.assertTrue("не удалось удалить"
                , clientUser.deleteUser(token).path("success"));
    }

    @Test
    @DisplayName("Успешная авторизация со всеми параметрами")
    public void loginUserFieldAllParamTest() {
        //Заполним все параметры
        loginUser.setEmail(AbstractTestApi.EMAIL);
        loginUser.setPassword(AbstractTestApi.PASSWORD);

        ValidatableResponse response = clientUser.loginUser(loginUser);
        response.statusCode(HttpStatus.SC_OK)
                        .and()
                        .body("accessToken", Matchers.notNullValue())
                        .and()
                        .body("refreshToken", Matchers.notNullValue())
                        .and()
                        .body("user", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Авторизация только с email без пароля")
    public void loginUserOnlyEmailParamTest() {
        //Заполним параметры
        loginUser.setEmail(AbstractTestApi.EMAIL);

        ValidatableResponse response = clientUser.loginUser(loginUser);

        response.statusCode(HttpStatus.SC_UNAUTHORIZED)
                        .and()
                        .body("success", Matchers.is(false))
                        .and()
                        .body("message", Matchers.equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация только с паролем без email")
    public void loginUserOnlyPasswordParamTest() {
        //Заполним все параметры
        loginUser.setPassword(AbstractTestApi.PASSWORD);

        ValidatableResponse response = clientUser.loginUser(loginUser);
        //Проверки
        response.statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с несуществующем пользователем")
    public void loginUserNotFoundTest() {
        String emailNoFound = "RyzhkovNotFound@diplom.ru";
        //Заполним все параметры
        loginUser.setEmail(emailNoFound);
        loginUser.setPassword(AbstractTestApi.PASSWORD);

        ValidatableResponse loginResponse = clientUser.loginUser(loginUser);
        Integer statusCode = loginResponse.extract().statusCode();

        //Если статус не 404, то возможно такой клиент в базе есть
        if (!statusCode.equals(404)) {
            //Тогда создадим цикл, где будем прибавлять циферки к логину
            int x = 1;
            while (!statusCode.equals(401)) {
                //Добавим в объект изменненное имя
                loginUser.setEmail(x + emailNoFound);
                //Пробуем еще раз
                loginResponse = clientUser.loginUser(loginUser);

                statusCode = loginResponse.extract().statusCode();
                //Вконце цикла увеличим число на единицу
                x++;
            }
        }

        //Проверки
        loginResponse.statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.equalTo("email or password are incorrect"));

    }

}