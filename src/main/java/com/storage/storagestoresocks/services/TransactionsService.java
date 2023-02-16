package com.storage.storagestoresocks.services;

import com.storage.storagestoresocks.models.Transaction;
import com.storage.storagestoresocks.models.enums.Color;
import com.storage.storagestoresocks.models.enums.Size;
import com.storage.storagestoresocks.models.enums.TypeTransaction;

import java.util.List;

public interface TransactionsService {
    void addTransactions(TypeTransaction typeTransaction,
                         int socksQuantity,
                         Size size,
                         int cotton,
                         Color color);

    List<Transaction> getAllTransactions();
}
