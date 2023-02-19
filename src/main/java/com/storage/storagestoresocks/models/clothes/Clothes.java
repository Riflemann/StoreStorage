package com.storage.storagestoresocks.models.clothes;

import com.storage.storagestoresocks.models.clothes.enums.*;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
@Data
@NoArgsConstructor
public class Clothes {

    TypeClothes typeClothes;

    Size size;

    Color color;

    @Max(value = 100, message = "Не может быть больше 100")
    @Positive(message = "Не может быть отрицательным")
    int cotton;

    int quantity;

    public Clothes(TypeClothes typeClothes, Size size, Color color, Object o, Object o1) {
    }
}
