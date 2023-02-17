package com.storage.storagestoresocks.services;

import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;


import java.util.List;
import java.util.Map;

public interface StorageService {
    void addSocksInStorage(Clothes clothes) throws QuantityException;

    List<Clothes> obtainAllSocks();

    Map<Integer, Clothes> obtainMapAllSocks();

    int getFromStock(Clothes clothes) throws QuantityException;

    int deleteFromStock(Clothes clothes) throws QuantityException;

    int availabilityCheck(Color color,
                          Size size,
                          int cottonMin,
                          int cottonMax);

}
