package com.storage.storagestoresocks.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.Socks;
import com.storage.storagestoresocks.models.enums.*;
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

    Map<Integer, Socks> storage = new HashMap<>();

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
    public void addSocksInStorage(Socks socks) throws QuantityException {
        if (!storage.containsValue(socks)) {
            storage.put(counter++, socks);
        } else {
            int key = checkQuantity(socks, storage);
            storage.get(key).setQuantity(storage.get(key).getQuantity() + socks.getQuantity());
        }

        fileService.saveToFile(fileStorageName, storage);

        transactionsService.addTransactions(
                TypeTransaction.INCOMING,
                socks.getQuantity(),
                socks.getSize(),
                socks.getCotton(),
                socks.getColor());

    }

    @Override
    public List<Socks> obtainAllSocks() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Map<Integer, Socks> obtainMapAllSocks() {
        return storage;
    }

    @Override
    public int getFromStock(Socks socks) throws QuantityException {

        transactionsService.addTransactions(
                TypeTransaction.OUTCOMING,
                socks.getQuantity(),
                socks.getSize(),
                socks.getCotton(),
                socks.getColor());

        return changeQuantityInStorage(socks);
    }

    @Override
    public int deleteFromStock(Socks socks) throws QuantityException {

        transactionsService.addTransactions(
                TypeTransaction.DEPRECATED,
                socks.getQuantity(),
                socks.getSize(),
                socks.getCotton(),
                socks.getColor());

        return changeQuantityInStorage(socks);
    }

    @Override
    public int availabilityCheck(Color color, Size size, int cottonMin, int cottonMax) {
        int quantity = 0;

        for (Socks socks : storage.values()) {
            if (socks.getColor() == color &&
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
            }
        }
        return key;
    }

    private void readFromFile() {
        try {
            if (Files.exists(Path.of(filePath, fileStorageName))) {
                String json = fileService.readFile(fileStorageName);
                storage = new ObjectMapper().readValue(json, new TypeReference<HashMap<Integer, Socks>>() {
                });
            } else {
                throw new FileNotFoundException();
            }
        } catch (JsonProcessingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int changeQuantityInStorage(Socks socks) throws QuantityException {

        int key = checkQuantity(socks, storage);
        int newQuantity = storage.get(key).getQuantity() - socks.getQuantity();
        storage.get(key).setQuantity(newQuantity);

        fileService.saveToFile(fileStorageName, storage);

        return newQuantity;
    }





}


