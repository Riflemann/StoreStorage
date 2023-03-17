package com.storage.storagestoresocks.models.clothes;

import com.storage.storagestoresocks.models.clothes.enums.*;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clothes {

    TypeClothes typeClothes;
    String brand;
    String model;
    Gender gender;
    Size size;
    Color color;
    @Max(value = 100, message = "Не может быть больше 100")
    @Positive(message = "Не может быть отрицательным")
    int cotton;
    int quantity;
}
