package site.nomoreparties.stellarburgers.orders;

import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.AbstractTestApi;
import site.nomoreparties.stellarburgers.clients.ClientOrder;
import site.nomoreparties.stellarburgers.clients.ClientUser;
import site.nomoreparties.stellarburgers.pojo.Ingredients;
import site.nomoreparties.stellarburgers.pojo.Order;
import site.nomoreparties.stellarburgers.pojo.User;

import java.util.ArrayList;

/*Создаем заказ, но так как айди ингредиентов проверяется
* сначала надо будет их получить*/
@DisplayName("Создание заказов")
public class CreateOrderTest {

    private Order order;
    private ClientOrder clientOrder;
    private Ingredients ingredients;
    //ингредиенты для создания ордера
    private ArrayList<String> ingredient;

    @Before
    public void beforeTest() {
        clientOrder = new ClientOrder();
        order = new Order();
        //cразу перед тестом получим ингредианты
        ingredients = clientOrder.getIngredients();
        ingredient = new ArrayList<>();
    }

    @Test
    @DisplayName("Создание заказа со всеми ингредиентами без авторизации")
    public void createOrderAllIngredientNoAuthTest() {
        //Добавляем сразу все ингредиенты
        for (int i=0; i < ingredients.getData().size(); i++) {
            ingredient.add(ingredients.getData().get(i).get_id());
        }
        order.setIngredients(ingredient);
        clientOrder.createOrder(order).statusCode(HttpStatus.SC_OK)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number",  Matchers.notNullValue());
    }

    /*Для этого теста придется создать пользователя и авторизоваться*/
    @Test
    @DisplayName("Создание заказа с ингредиентами и авторизацией")
    public void createOrderWithIngredientWithAuthTest() {
        ClientUser clientUser = new ClientUser();
        //Получили токен
        String accessToken =
                clientUser.createUser(new User(AbstractTestApi.EMAIL, AbstractTestApi.PASSWORD, AbstractTestApi.NAME))
                        .extract().path("accessToken");

        //Добавляем часть ингредиентов
        for (int i=0; i < 5; i++) {
            ingredient.add(ingredients.getData().get(i).get_id());
        }
        order.setIngredients(ingredient);
        //Создаем заказ с авторизацией
        clientOrder.createOrder(order, accessToken).statusCode(HttpStatus.SC_OK)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number",  Matchers.notNullValue());

        //Теперь за собой нужно удалить пользователя
        clientUser.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderNoIngredientTest() {
        //Пустой список ингредиентов
        order.setIngredients(ingredient);
        clientOrder.createOrder(order).statusCode(HttpStatus.SC_BAD_REQUEST)
                .and().body("message", Matchers.equalTo("Ingredient ids must be provided"));
    }

}
