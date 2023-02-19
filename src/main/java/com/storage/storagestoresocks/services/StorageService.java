package com.storage.storagestoresocks.services;

import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Map;

public interface StorageService {
    void addClothesInStorage(Clothes clothes) throws QuantityException;

    List<Clothes> obtainAllClothes();

    Map<Integer, Clothes> obtainMapAllClothes();

    int getFromStock(Clothes clothes) throws QuantityException;

    int deleteFromStock(Clothes clothes) throws QuantityException;

    int availabilityCheck(Color color,
                          Size size,
                          TypeClothes typeClothes,
                          int cottonMin,
                          int cottonMax);

}
