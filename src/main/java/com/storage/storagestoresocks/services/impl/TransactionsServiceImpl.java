package com.storage.storagestoresocks.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storage.storagestoresocks.models.Transaction;
import com.storage.storagestoresocks.models.clothes.enums.*;
import com.storage.storagestoresocks.services.FileService;
import com.storage.storagestoresocks.services.TransactionsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    private final FileService fileService;

    @Value("${name.of.transaction.file}")
    private String fileTransactionName;

    @Value("${path.to.file.folder}")
    private String filePath;

    int counter;

    Map<Integer, Transaction> transactionsMap = new HashMap<>();
    ArrayList<Transaction> transactionArrayList = new ArrayList<>();

    @PostConstruct
    @Override
    public void init() {
        readFromFile();
        counter = transactionsMap.size();
    }

    public TransactionsServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void addTransactions(TypeTransaction typeTransaction, TypeClothes typeClothes,
                                int socksQuantity, int cotton) {

        transactionsMap.put(counter, new Transaction.TransactionBuilder().
                typeTransaction(typeTransaction).
//                iD(counter++).
                typeClothes(typeClothes.toString()).
                createTime(LocalDateTime.now()).
                cotton(cotton).
//                size(size).
//                color(color).
                clothesQuantity(socksQuantity).
                build());

        fileService.saveToFile(fileTransactionName, transactionsMap);

        System.out.println("Транзакция № " + (counter - 1) + " добавлена");
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactionsMap.values());
    }

    @Override
    public List<Transaction> extractList(@RequestParam(required = false) String fromDate,
                            @RequestParam(required = false) String toDate,
                            int cottonMin,
                            int cottonMax) {

        transactionArrayList.clear();
        LocalDateTime lDTFromDate;
        LocalDateTime lDTToDate;


        if (cottonMax == 0) {
            cottonMax = 100;
        }

        if (fromDate != null && toDate != null) {
            lDTFromDate = LocalDate.parse(fromDate, StorageServiceImpl.FORMAT_DATE).atStartOfDay();
            lDTToDate = LocalDate.parse(toDate, StorageServiceImpl.FORMAT_DATE).atStartOfDay();
        } else {
            lDTFromDate = LocalDateTime.now().minusYears(1L);
            lDTToDate = LocalDateTime.now();
        }

        for (Transaction transaction : transactionsMap.values()) {
            LocalDateTime lDTTransaction = LocalDate.parse(transaction.getCreateTime(), StorageServiceImpl.FORMAT_DATE).atTime(12, 0);
            if (lDTTransaction.isAfter(lDTFromDate) &&
                    lDTTransaction.isBefore(lDTToDate) &&
                    transaction.getCotton() <= cottonMax &&
                    transaction.getCotton() >= cottonMin) {

                transactionArrayList.add(transaction);
            }
        }
        return transactionArrayList;
    }

    @Override
    public int calculateQuantity(@RequestParam(required = false) Color color,
                                 @RequestParam(required = false) Size size,
                                 @RequestParam(required = false) TypeClothes typeClothes) {
        int quantity = 0;
        for (Transaction transaction : transactionArrayList) {

            if (color == null && size == null && typeClothes == null) {

                quantity += transaction.getClothesQuantity();

            } else if (size == null &&
                    transaction.getColor() == color &&
                    transaction.getTypeClothes() == typeClothes) {

                quantity += transaction.getClothesQuantity();

            } else if (typeClothes == null &&
                    transaction.getColor() == color &&
                    transaction.getSize() == size) {

                quantity += transaction.getClothesQuantity();

            } else if (transaction.getColor() == color &&
                    transaction.getSize() == size &&
                    transaction.getTypeClothes() == typeClothes) {

                quantity += transaction.getClothesQuantity();
            }
        }


        return quantity;
    }


    private void readFromFile() {
        try {
            if (Files.exists(Path.of(filePath, fileTransactionName))) {
                String json = fileService.readFile(fileTransactionName);
                transactionsMap = new ObjectMapper().readValue(json, new TypeReference<HashMap<Integer, Transaction>>() {
                });
            } else {
                System.out.println("Отсутсвует инициирующий фаил Transaction2.json");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

