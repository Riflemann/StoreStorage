package com.storage.storagestoresocks.services;

import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface StorageService {
    void addSocksInStorage(Clothes clothes) throws QuantityException;

    List<Clothes> obtainAllSocks();

    int getFromStock(Clothes clothes) throws QuantityException;

    int deleteFromStock(Clothes socks) throws QuantityException;

    int availabilityCheck(Color color,
                          Size size,
                          int cottonMin,
                          int cottonMax);
}
