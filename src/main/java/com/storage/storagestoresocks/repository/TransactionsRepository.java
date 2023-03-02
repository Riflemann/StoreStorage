package com.storage.storagestoresocks.repository;

import com.storage.storagestoresocks.models.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionsRepository {

    List<Transaction> findAll();

    Optional<Transaction> findById(String id);

    Transaction save(Transaction transaction);
}
