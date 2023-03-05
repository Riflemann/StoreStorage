package com.storage.storagestoresocks.services.impl;

import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;

class StorageServiceImplTest {


    @Test
    void addClothesInStorage() throws QuantityException {
        StorageServiceImpl storageService = new StorageServiceImpl(new TransactionsServiceImpl(new FileServiceImpl()), new FileServiceImpl());

        Clothes clothes = new Clothes(TypeClothes.SOCKS, Size.SIZE_M, Color.BLUE, 50, 500);

        storageService.addClothesInStorage(clothes);

        Assertions.assertTrue(storageService.storage.size()>0);
    }
}