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
    public void getIngredientsTest() {
        ClientOrder clientOrder = new ClientOrder();

        Ingredients ingredients = clientOrder.getIngredients();
        Assert.assertTrue(ingredients.getSuccess());
        //Просто проверим, что всего 15 ингредиентов, каких не важно.
        Assert.assertEquals(15 , ingredients.getData().size());
    }
}
