package site.nomoreparties.stellarburgers.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.AbstractTestApi;
import site.nomoreparties.stellarburgers.pojo.Ingredients;
import site.nomoreparties.stellarburgers.pojo.Order;


import static io.restassured.RestAssured.given;

//Класс для указния методов для заказов
public class ClientOrder extends AbstractTestApi{

        private static final String ORDER_ENDPOINT = "/orders";
        private static final String GET_INGREDIENTS_ENDPOINT = "/ingredients";

        @Step("createOrderNoAuth")
        public ValidatableResponse createOrder(Order order) {
            return given()
                    .spec(getSpec())
                    .body(order) //передаем инфу по курьеру
                    .when()
                    .post(ORDER_ENDPOINT)
                    .then();
        }

        @Step("createOrderWithAuth")
        public ValidatableResponse createOrder(Order order, String accessToken) {
                return given()
                        .spec(getSpec())
                        .header("Authorization", accessToken)
                        .body(order) //передаем инфу по курьеру
                        .when()
                        .post(ORDER_ENDPOINT)
                        .then();
        }

        @Step("getIngredients")
        public Ingredients getIngredients() {
                return given()
                        .spec(getSpec())
                        .when()
                        .get(GET_INGREDIENTS_ENDPOINT)
                        .body()
                        .as(Ingredients.class);
        }

        @Step("GetOrderUserWithAuth")
        public ValidatableResponse getOrderUser(String accessToken) {
                return given()
                        .spec(getSpec())
                        .header("Authorization", accessToken)
                        .when()
                        .get(ORDER_ENDPOINT)
                        .then();
        }

        @Step("GetOrderUserNoAuth")
        public ValidatableResponse getOrderUser() {
                return given()
                        .spec(getSpec())
                        .when()
                        .get(ORDER_ENDPOINT)
                        .then();
        }

}
