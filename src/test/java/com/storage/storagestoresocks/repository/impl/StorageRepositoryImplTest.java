package com.storage.storagestoresocks.repository.impl;

import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.when;

class StorageRepositoryImplTest {

    StorageRepositoryImpl storageRepository;

    @Test
    void findAll() {
        List<Clothes> clothesList = Arrays.asList(
                new Clothes(TypeClothes.SOCKS, Size.SIZE_M, Color.BLUE, 50, 500),
                new Clothes(TypeClothes.SOCKS, Size.SIZE_M, Color.BLUE, 50, 500)
        );
        when(storageRepository.findAll())
                .thenReturn(clothesList);
    }

    @Test
    void findAllTest() {
        List<Clothes> clothesList = new ArrayList<>();
        clothesList = storageRepository.findAll();
        System.out.println(clothesList);
    }
}