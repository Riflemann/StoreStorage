package com.storage.storagestoresocks.services;

import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.Socks;
import com.storage.storagestoresocks.models.enums.Color;
import com.storage.storagestoresocks.models.enums.Size;

import java.util.List;
import java.util.Map;

public interface StorageService {
    void addSocksInStorage(Socks socks) throws QuantityException;

    abstract List<Socks> obtainAllSocks();

    Map<Integer, Socks> obtainMapAllSocks();

    int getFromStock(Socks socks) throws QuantityException;

    int deleteFromStock(Socks socks) throws QuantityException;

    int availabilityCheck(Color color,
                          Size size,
                          int cottonMin,
                          int cottonMax);

}
