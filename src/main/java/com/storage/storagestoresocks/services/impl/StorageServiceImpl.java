package com.storage.storagestoresocks.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.*;
import com.storage.storagestoresocks.repository.StorageRepository;
import com.storage.storagestoresocks.services.FileService;
import com.storage.storagestoresocks.services.StorageService;
import com.storage.storagestoresocks.services.TransactionsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageServiceImpl implements StorageService {

    final static DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private final TransactionsService transactionsService;

    private final FileService fileService;

    private StorageRepository storageRepository;

    static int counter;

    @Value("${name.of.storage.file}")
    private String fileStorageName;

    @Value("${path.to.file.folder}")
    private String filePath;

    Map<Integer, Clothes> storage = new HashMap<>();

    @PostConstruct
    @Override
    public void init() {
        readFromFile();
        counter = storage.size();
        storage.values().forEach(e -> storageRepository.save(e));
    }

    public StorageServiceImpl(TransactionsService transactionsService, FileService fileService, StorageRepository storageRepository) {
        this.transactionsService = transactionsService;
        this.fileService = fileService;
        this.storageRepository = storageRepository;
    }

    @Override
    public void addClothesInStorage(Clothes clothes)  {
        storageRepository.save(clothes);
    }

    @Override
    public List<Clothes> obtainAllClothes() {
        return storageRepository.findAll();
    }

    @Override
    public Map<Integer, Clothes> obtainMapAllClothes() {
        return storage;
    }

//    @Override
//    public int getFromStock(Clothes clothes) throws QuantityException {
//
//        transactionsService.addTransactions(
//                TypeTransaction.OUTCOMING,
//                clothes.getTypeClothes(),
//                clothes.getQuantity(),
////                clothes.getSize(),
////                clothes.getColor());
//                clothes.getCotton());
//
//        return changeQuantityInStorage(clothes);
//    }

//    @Override
//    public int deleteFromStock(Clothes clothes) throws QuantityException {
//
//        transactionsService.addTransactions(
//                TypeTransaction.DEPRECATED,
//                clothes.getTypeClothes(),
//                clothes.getQuantity(),
//                clothes.getCotton());
////                clothes.getSize(),
////                clothes.getColor());
//
//        return changeQuantityInStorage(clothes);
//    }

    @Override
    public int availabilityCheck(TypeClothes typeClothes, String brand, String model, Gender gender,
                                 Color color, Size size, int cottonMin, int cottonMax) {
//        return storage.values().stream()
//                .filter(clothes ->
//                        (color == null || color == clothes.getColor())
//                                && (size == null || size == clothes.getSize())
//                                && (typeClothes == null || typeClothes == clothes.getTypeClothes())
//                                && clothes.getCotton() > cottonMin
//                                && clothes.getCotton() < cottonMax)
//                .map(Clothes::getQuantity)
//                .mapToInt(Integer::valueOf)
//                .sum();
        return storageRepository.availabilityCheck(typeClothes, brand, model, gender, size, color, cottonMin, cottonMax);
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
                System.out.println("Отсутсвует инициирующий фаил Storage2.json");
            }
        } catch (JsonProcessingException e) {
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


