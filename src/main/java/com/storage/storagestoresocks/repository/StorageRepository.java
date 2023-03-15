package com.storage.storagestoresocks.repository;

import com.storage.storagestoresocks.exceptions.NotFoundException;
import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StorageRepository {

    List<Clothes> findAll();

    Optional<Clothes> findById(String id);

    Clothes save(Clothes clothes);

    int[] batchUpdate(List<Clothes> clothes);

    int availabilityCheck(TypeClothes typeClothes, Size size, Color color, int cottonMin, int cottonMax);

    Clothes obtainFromStorage(Clothes clothes) throws NotFoundException, QuantityException;
}
