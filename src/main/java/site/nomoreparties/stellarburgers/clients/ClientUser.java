package site.nomoreparties.stellarburgers.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.AbstractTestApi;
import site.nomoreparties.stellarburgers.pojo.LoginUser;
import site.nomoreparties.stellarburgers.pojo.User;

import static io.restassured.RestAssured.given;

//Класс для указния методов для пользователя
public class ClientUser extends AbstractTestApi {
    private static final String USER_CREATE_ENDPOINT = "/auth/register";
    private static final String USER_AUTH_ENDPOINT = "/auth/user";
    private static final String USER_LOGIN_ENDPOINT = "/auth/login";

    @Step("createUser")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user) //передаем инфу по курьеру
                .when()
                .post(USER_CREATE_ENDPOINT)
                .then();
    }

    @Step("loginUser")
    public ValidatableResponse loginUser(LoginUser loginUser) {
        return given()
                .spec(getSpec())
                .body(loginUser) //передаем инфу по курьеру
                .when()
                .post(USER_LOGIN_ENDPOINT)
                .then();
    }


    @Step("deleteUser")
    public Response deleteUser(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_AUTH_ENDPOINT); //"https://stellarburgers.nomoreparties.site/api/auth/user"
    }


    @Step("getDataUser")
    public Response getDataUser(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .get(USER_AUTH_ENDPOINT)
                ;
    }


    @Step("changeDataWithAuthUser")
    public Response changeDataUser(String accessToken, User user) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_AUTH_ENDPOINT);
    }

    @Step("changeDataNoAuthUser")
    public Response changeDataUser( User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(USER_AUTH_ENDPOINT);
    }

}
