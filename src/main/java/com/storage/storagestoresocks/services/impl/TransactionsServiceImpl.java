package com.storage.storagestoresocks.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storage.storagestoresocks.models.Transaction;
import com.storage.storagestoresocks.models.enums.*;
import com.storage.storagestoresocks.services.FileService;
import com.storage.storagestoresocks.services.TransactionsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public void addTransactions(TypeTransaction typeTransaction, int socksQuantity,
                                Size size, int cotton, Color color) {

        transactionsMap.put(counter, new Transaction.TransactionBuilder().
                typeTransaction(typeTransaction).
                iD(counter++).
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
