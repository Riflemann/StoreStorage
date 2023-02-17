package com.storage.storagestoresocks.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import com.storage.storagestoresocks.models.clothes.enums.TypeTransaction;
import com.storage.storagestoresocks.services.FileService;
import com.storage.storagestoresocks.services.StorageService;
import com.storage.storagestoresocks.services.TransactionsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageServiceImpl implements StorageService {

    private final TransactionsService transactionsService;

    private final FileService fileService;

    static int counter;

    @Value("${name.of.storage.file}")
    private String fileStorageName;

    @Value("${path.to.file.folder}")
    private String filePath;

    Map<Integer, Clothes> storage = new HashMap<>();

    @PostConstruct
    private void init() {
        readFromFile();
        counter = storage.size();
    }

    public StorageServiceImpl(TransactionsService transactionsService, FileService fileService) {
        this.transactionsService = transactionsService;
        this.fileService = fileService;
    }

    @Override
    public void addClothesInStorage(Clothes clothes) throws QuantityException {
        if (!storage.containsValue(clothes)) {
            storage.put(counter++, clothes);
        } else {
            int key = checkQuantity(clothes, storage);
            storage.get(key).setQuantity(storage.get(key).getQuantity() + clothes.getQuantity());
        }

        fileService.saveToFile(fileStorageName, storage);

        transactionsService.addTransactions(
                TypeTransaction.INCOMING,
                clothes.getTypeClothes(),
                clothes.getQuantity(),
                clothes.getSize(),
                clothes.getCotton(),
                clothes.getColor());

    }

    @Override
    public List<Clothes> obtainAllClothes() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Map<Integer, Clothes> obtainMapAllClothes() {
        return storage;
    }

    @Override
    public int getFromStock(Clothes clothes) throws QuantityException {

        transactionsService.addTransactions(
                TypeTransaction.OUTCOMING,
                clothes.getTypeClothes(),
                clothes.getQuantity(),
                clothes.getSize(),
                clothes.getCotton(),
                clothes.getColor());

        return changeQuantityInStorage(clothes);
    }

    @Override
    public int deleteFromStock(Clothes clothes) throws QuantityException {

        transactionsService.addTransactions(
                TypeTransaction.DEPRECATED,
                clothes.getTypeClothes(),
                clothes.getQuantity(),
                clothes.getSize(),
                clothes.getCotton(),
                clothes.getColor());

        return changeQuantityInStorage(clothes);
    }

    @Override
    public int availabilityCheck(@RequestParam(required = false) Color color,
                                 @RequestParam(required = false) Size size,
                                 @RequestParam(required = false) TypeClothes typeClothes,
                                 int cottonMin,
                                 int cottonMax) {
        int quantity = 0;

/*
*  todo проверить работу методава
* */

        if (color == null) {
            for (Clothes clothes : storage.values()) {
                if (clothes.getSize() == size &&
                        clothes.getTypeClothes() == typeClothes &&
                        clothes.getCotton() > cottonMin &&
                        clothes.getCotton() < cottonMax) {

                    quantity += clothes.getQuantity();

                }
            }
        } else if (size == null) {
            for (Clothes clothes : storage.values()) {
                if (clothes.getColor() == color &&
                        clothes.getTypeClothes() == typeClothes &&
                        clothes.getCotton() > cottonMin &&
                        clothes.getCotton() < cottonMax) {

                    quantity += clothes.getQuantity();

                }
            }
        } else if (typeClothes == null) {
            for (Clothes clothes : storage.values()) {
                if (clothes.getColor() == color &&
                        clothes.getSize() == size &&
                        clothes.getCotton() > cottonMin &&
                        clothes.getCotton() < cottonMax) {

                    quantity += clothes.getQuantity();

                }
            }
        } else {
            for (Clothes clothes : storage.values()) {
                if (clothes.getColor() == color &&
                        clothes.getSize() == size &&
                        clothes.getTypeClothes() == typeClothes &&
                        clothes.getCotton() > cottonMin &&
                        clothes.getCotton() < cottonMax) {

                    quantity += clothes.getQuantity();

                }
            }
        }

        return quantity;
    }

    public static int checkQuantity(Clothes clothes, Map<Integer, Clothes> map) throws QuantityException {
        int key = 0;

        if (clothes.getQuantity() <= 0) {
            throw new QuantityException("Не указано количество носков");
        }

        for (Map.Entry<Integer, Clothes> clothesEntry : map.entrySet()) {
            if (clothesEntry.getValue().equals(clothes)) {
                key = clothesEntry.getKey();
                if (clothesEntry.getValue().getQuantity() < clothes.getQuantity()) {
                    throw new QuantityException("Указанного количества нет не складе");
                }
            }
        }
        return key;
    }

    private void readFromFile() {
        try {
            if (Files.exists(Path.of(filePath, fileStorageName))) {
                String json = fileService.readFile(fileStorageName);
                storage = new ObjectMapper().readValue(json, new TypeReference<HashMap<Integer, Clothes>>() {
                });
            } else {
                throw new FileNotFoundException();
            }
        } catch (JsonProcessingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int changeQuantityInStorage(Clothes clothes) throws QuantityException {

        int key = checkQuantity(clothes, storage);
        int newQuantity = storage.get(key).getQuantity() - clothes.getQuantity();
        storage.get(key).setQuantity(newQuantity);

        fileService.saveToFile(fileStorageName, storage);

        return newQuantity;
    }


}


