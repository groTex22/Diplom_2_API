package site.nomoreparties.stellarburgers.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class Ingredients {
    private Boolean success;
    private ArrayList<Ingredient> data;

    /*Все в одном классе, так как нигде больше использовать не собираемся*/
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Ingredient {
        private String _id;
        private String name;
        private String type;
        private Integer proteins;
        private Integer fat;
        private Integer carbohydrates;
        private Integer calories;
        private Integer price;
        private String image;
        private String image_mobile;
        private String image_large;
        private Integer __v;
    }

}
