package com.storage.storagestoresocks.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.*;
import com.storage.storagestoresocks.services.FileService;
import com.storage.storagestoresocks.services.StorageService;
import com.storage.storagestoresocks.services.TransactionsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public void addSocksInStorage(Clothes clothes) throws QuantityException {
        if (!storage.containsValue(clothes)) {
            storage.put(counter++, clothes);
        } else {
            int key = checkQuantity(clothes, storage);
            storage.get(key).setQuantity(storage.get(key).getQuantity() + clothes.getQuantity());
        }

        fileService.saveToFile(fileStorageName, storage);

        transactionsService.addTransactions(
                TypeTransaction.INCOMING,
                clothes.getQuantity(),
                clothes.getSize(),
                clothes.getCotton(),
                clothes.getColor());

    }

    @Override
    public List<Clothes> obtainAllSocks() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int getFromStock(Clothes clothes) throws QuantityException {

        transactionsService.addTransactions(
                TypeTransaction.OUTCOMING,
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
                clothes.getQuantity(),
                clothes.getSize(),
                clothes.getCotton(),
                clothes.getColor());

        return changeQuantityInStorage(clothes);
    }

    @Override
    public int availabilityCheck(Color color, Size size, int cottonMin, int cottonMax) {
        int quantity = 0;

        for (Clothes clothes : storage.values()) {
            if (clothes.getColor() == color &&
                    clothes.getSize() == size &&
                    clothes.getCotton() > cottonMin &&
                    clothes.getCotton() < cottonMax) {

                quantity += clothes.getQuantity();

            }
        }

        return quantity;
    }


    public static int checkQuantity(Clothes socks, Map<Integer, Clothes> map) throws QuantityException {
        int key = 0;

        if (socks.getQuantity() <= 0) {
            throw new QuantityException("Не указано количество носков");
        }

        for (Map.Entry<Integer, Clothes> socksEntry : map.entrySet()) {
            if (socksEntry.getValue().equals(socks)) {
                key = socksEntry.getKey();
                if (socksEntry.getValue().getQuantity() < socks.getQuantity()) {
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





