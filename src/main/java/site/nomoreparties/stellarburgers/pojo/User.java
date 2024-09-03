package site.nomoreparties.stellarburgers.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс для преобразования JSON
//Сериализация, десириализация
//Используем ломбок, так действительно лаконичнее
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;

}
