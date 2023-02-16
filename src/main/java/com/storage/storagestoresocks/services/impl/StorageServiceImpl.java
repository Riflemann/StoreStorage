package com.storage.storagestoresocks.services.impl;

import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.Socks;
import com.storage.storagestoresocks.models.enums.Color;
import com.storage.storagestoresocks.models.enums.Size;
import com.storage.storagestoresocks.services.StorageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageServiceImpl implements StorageService {
    static int counter;

    Map<Integer, Socks> storage = new HashMap<>();

    @Override
    public void addSocksInStorage(Socks socks) throws QuantityException {
        if (!storage.containsValue(socks)) {
            storage.put(counter++, socks);
        } else {
            int key = checkQuantity(socks, storage);
            storage.get(key).setQuantity(storage.get(key).getQuantity() + socks.getQuantity());
        }


    }

    @Override
    public List<Socks> obtainAllSocks() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int getFromStock(Socks socks) throws QuantityException {
        int key = checkQuantity(socks, storage);
        int onStock = storage.get(key).getQuantity() - socks.getQuantity();
        storage.get(key).setQuantity(storage.get(key).getQuantity() - socks.getQuantity());
        return onStock;
    }

    @Override
    public int deleteFromStock(Socks socks) throws QuantityException {
        int key = checkQuantity(socks, storage);
        int deleted = storage.get(key).getQuantity() - socks.getQuantity();
        storage.get(key).setQuantity(storage.get(key).getQuantity() - socks.getQuantity());
        return deleted;
    }

    @Override
    public int availabilityCheck(Color color,
                                 Size size,
                                 int cottonMin,
                                 int cottonMax) {
        int quantity = 0;

        for (Socks socks : storage.values()) {
            if (    socks.getColor() == color &&
                    socks.getSize() == size &&
                    socks.getCotton() > cottonMin &&
                    socks.getCotton() < cottonMax) {

                quantity += socks.getQuantity();

            }
        }

        return quantity;
    }


    public static int checkQuantity(Socks socks, Map<Integer, Socks> map) throws QuantityException {
        int key = 0;

        if (socks.getQuantity() <= 0) {
            throw new QuantityException("Не указано количество носков");
        }

        for (Map.Entry<Integer, Socks> socksEntry : map.entrySet()) {
            if (socksEntry.getValue().equals(socks)) {
                key = socksEntry.getKey();
                if (socksEntry.getValue().getQuantity() < socks.getQuantity()) {
                    throw new QuantityException("Указанного количества нет не складе");
                }
            } else {
                throw new QuantityException("Данных носков нет не складе");
            }
        }
        return key;
    }
}
