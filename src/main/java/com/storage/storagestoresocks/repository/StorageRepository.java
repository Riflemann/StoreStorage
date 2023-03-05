package com.storage.storagestoresocks.repository;

import com.storage.storagestoresocks.models.clothes.Clothes;

import java.util.List;
import java.util.Optional;

public interface StorageRepository {

    List<Clothes> findAll();

    Optional<Clothes> findById(String id);

    Clothes save(Clothes clothes);

}
