package com.storage.storagestoresocks.models.clothes;

import com.storage.storagestoresocks.models.clothes.enums.*;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
@Getter
@Setter
@EqualsAndHashCode
public abstract class Clothes {

    Size size;

    Color color;

    @Max(value = 100, message = "Не может быть больше 100")
    @Positive(message = "Не может быть отрицательным")
    int cotton;

    int quantity;
}
