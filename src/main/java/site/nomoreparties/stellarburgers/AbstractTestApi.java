package site.nomoreparties.stellarburgers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class AbstractTestApi {

    protected static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";

    //константы для email, password, name
    public static final String EMAIL = "test_ryzkov@diplom.ru";
    public static final String PASSWORD = "password_12!";
    public static final String NAME = "fluffy qa";

    //Создает спецификацию для запроса на эндпоинт
    protected static RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON) //Тип JSON
                .setBaseUri(BASE_URL) //Урла что тестим
                .build();
    }
}
