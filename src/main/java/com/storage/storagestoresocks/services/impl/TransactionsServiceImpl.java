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
import java.io.FileNotFoundException;
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

    @PostConstruct
    private void init() {
        readFromFile();
        counter = transactionsMap.size();
    }

    public TransactionsServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void addTransactions(TypeTransaction typeTransaction, TypeClothes typeClothes,
                                int socksQuantity, Size size, int cotton, Color color) {

        transactionsMap.put(counter, new Transaction.TransactionBuilder().
                typeTransaction(typeTransaction).
                iD(counter++).
                typeClothes(typeClothes).
                createTime(LocalDateTime.now()).
                cotton(cotton).
                size(size).
                color(color).
                socksQuantity(socksQuantity).
                build());

        fileService.saveToFile(fileTransactionName, transactionsMap);

        System.out.println("Транзакция № " + (counter - 1) + " добавлена");
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactionsMap.values());
    }

    @Override
    public int availabilityCheck(@RequestParam(required = false) Color color,
                                 @RequestParam(required = false) Size size,
                                 @RequestParam(required = false) TypeClothes typeClothes,
                                 @RequestParam(required = false) String fromDate,
                                 @RequestParam(required = false) String toDate,
                                 int cottonMin,
                                 int cottonMax) {

        int quantity = 0;
        LocalDateTime lDTFromDate;
        LocalDateTime lDTToDate;
        ArrayList<Transaction> clothesArrayList = new ArrayList<>();
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
                    transaction.getCotton() < cottonMax &&
                    transaction.getCotton() > cottonMin) {

                clothesArrayList.add(transaction);
            }
        }

        Transaction transactionTemp = new Transaction();
        transactionTemp.setSize(size);
        transactionTemp.setColor(color);
        transactionTemp.setTypeClothes(typeClothes);
        System.out.println("проверка объекта в TransactionsServiceImpl.availabilityCheck " + transactionTemp);

        for (Transaction transaction : clothesArrayList) {
            if (transaction.equals(transactionTemp)) {
                quantity += transaction.getClothesQuantity();
            }
        }

            return quantity;
        }

        private void readFromFile () {
            try {
                if (Files.exists(Path.of(filePath, fileTransactionName))) {
                    String json = fileService.readFile(fileTransactionName);
                    transactionsMap = new ObjectMapper().readValue(json, new TypeReference<HashMap<Integer, Transaction>>() {
                    });
                } else {
                    throw new FileNotFoundException();
                }
            } catch (JsonProcessingException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

