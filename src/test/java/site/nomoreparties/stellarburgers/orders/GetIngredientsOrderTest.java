package site.nomoreparties.stellarburgers.orders;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;
import site.nomoreparties.stellarburgers.clients.ClientOrder;
import site.nomoreparties.stellarburgers.pojo.Ingredients;

//Проверим, что эндпоинт для получения ингредиентов работает.
@DisplayName("Получение ингредиентов")
public class GetIngredientsOrderTest {

    @Test
    @DisplayName("Получение ингредиентов")
    public void getIngredientsTest() {
        ClientOrder clientOrder = new ClientOrder();

        Ingredients ingredients = clientOrder.getIngredients();
        Assert.assertTrue(ingredients.getSuccess());
        //И тег data не пустой, большего пока не требовалось
        //to_do: при необходимости можно будет проверить на null каждый тег одного из ингредиентов
        Assert.assertNotNull(ingredients.getData());

    }
}
