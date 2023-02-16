package com.storage.storagestoresocks.services.impl;

import com.storage.storagestoresocks.models.Transaction;
import com.storage.storagestoresocks.models.enums.*;
import com.storage.storagestoresocks.services.TransactionsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    int counter;

    HashMap<Integer, Transaction> transactionsMap;

    @Override
    public void addTransactions(TypeTransaction typeTransaction,
                                int socksQuantity,
                                Size size,
                                int cotton,
                                Color color) {

        transactionsMap.put(counter, new Transaction.TransactionBuilder().
                                                                        typeTransaction(typeTransaction).
                                                                        iD(counter++).
                                                                        createTime(LocalDateTime.now()).
                                                                        cotton(cotton).
                                                                        size(size).
                                                                        color(color).
                                                                        socksQuantity(socksQuantity).
                                                                        build());
        System.out.println("Транзакция № " + (counter - 1) + " добавлена");
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactionsMap.values());
    }


}
