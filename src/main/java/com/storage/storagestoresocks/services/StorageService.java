package com.storage.storagestoresocks.services;

import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Gender;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import org.springframework.web.bind.annotation.RequestParam;


import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

public interface StorageService {
    @PostConstruct
    void init();


    void addClothesInStorage(Clothes clothes) throws QuantityException;

    List<Clothes> obtainAllClothes();

    Map<Integer, Clothes> obtainMapAllClothes();

//    int getFromStock(Clothes clothes) throws QuantityException;
//
//    int deleteFromStock(Clothes clothes) throws QuantityException;

    int availabilityCheck(TypeClothes typeClothes, String brand, String model, Gender gender,
                          Color color, Size size, int cottonMin, int cottonMax);

}
