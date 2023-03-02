package com.storage.storagestoresocks.repository.impl;

import com.storage.storagestoresocks.models.Transaction;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import com.storage.storagestoresocks.models.clothes.enums.TypeTransaction;
import com.storage.storagestoresocks.repository.TransactionsRepository;
import com.storage.storagestoresocks.services.impl.StorageServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TransactionsRepositoryImplTest {



    TransactionsRepository transactionsRepository;

    String date = "02.03.2023";
    LocalDateTime lcd = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).atStartOfDay();

    Transaction transaction = new Transaction.TransactionBuilder()
            .iD(1)
            .typeTransaction(TypeTransaction.INCOMING)
            .typeClothes(TypeClothes.SOCKS)
            .createTime(lcd)
            .clothesQuantity(500)
            .build();

    public TransactionsRepositoryImplTest(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    @Test
    void save() {
        Assertions.assertEquals(transaction, transactionsRepository.save(transaction));

    }
}