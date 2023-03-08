package com.storage.storagestoresocks.repository.impl;

import com.storage.storagestoresocks.models.Transaction;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import com.storage.storagestoresocks.models.clothes.enums.TypeTransaction;
import com.storage.storagestoresocks.repository.TransactionsRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionsRepositoryImpl implements TransactionsRepository {

    private JdbcTemplate jdbcTemplate;

    public TransactionsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Transaction> findAll() {
        List<Transaction> list = null;
        try {
            list = jdbcTemplate.query(
                    "select * from transactions_rep",
                    this::mapRowToTransaction);
        } catch (DataAccessException e) {
            System.out.println(e);
        }

        return list;
    }

    @Override
    public Optional<Transaction> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Transaction save(Transaction transaction) {
        jdbcTemplate.update(
                "insert into transactions_rep (typeTransaction,typeClothes,createTime, clothesQuantity) values(?, ?, ?, ?)",
                transaction.getTypeTransaction().toString(),
                transaction.getTypeClothes().toString(),
                Timestamp.valueOf(LocalDateTime.parse(transaction.getCreateTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))),
                transaction.getClothesQuantity());
        return transaction;
    }

    private Transaction mapRowToTransaction(ResultSet row, int rowNum)
            throws SQLException {
        return new Transaction.TransactionBuilder().
                typeTransaction(TypeTransaction.valueOf(row.getString("typeTransaction"))).
                typeClothes(TypeClothes.valueOf(row.getString("typeClothes")).toString()).
                createTime(row.getTimestamp("createTime").toLocalDateTime()).
                clothesQuantity(row.getInt("clothesQuantity"))
                .build();
    }
}
