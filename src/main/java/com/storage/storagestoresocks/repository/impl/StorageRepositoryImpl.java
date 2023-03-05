package com.storage.storagestoresocks.repository.impl;

import com.storage.storagestoresocks.models.Transaction;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import com.storage.storagestoresocks.models.clothes.enums.TypeTransaction;
import com.storage.storagestoresocks.repository.StorageRepository;
import com.storage.storagestoresocks.repository.TransactionsRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class StorageRepositoryImpl implements StorageRepository {
    private JdbcTemplate jdbcTemplate;

    private TransactionsRepository transactionsRepository;

    public StorageRepositoryImpl(JdbcTemplate jdbcTemplate, TransactionsRepository transactionsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public List<Clothes> findAll() {
        List<Clothes> list = null;
        try {
            list = jdbcTemplate.query(
                    "select * from CLOTHES_REP",
                    this::mapRowToClothes);
        } catch (DataAccessException e) {
            System.out.println(e);
        }

        return list;
    }

    @Override
    public Optional<Clothes> findById(String id) {
        List<Clothes> results = jdbcTemplate.query(
                "select id, type_Clothes, size, color, cotton, quantity from CLOTHES_REP where id=?",
                this::mapRowToClothes,
                id);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    @Override
    public Clothes save(Clothes clothes) {

        int idTr = 0;

        transactionsRepository.save(new Transaction.TransactionBuilder()
                .typeTransaction(TypeTransaction.INCOMING)
                .typeClothes(clothes.getTypeClothes())
                .createTime(LocalDateTime.now())
                .clothesQuantity(clothes.getQuantity())
                .build());

        String sqlFindInDB = "select * from CLOTHES_REP " +
                "where type_Clothes=" + "'" + clothes.getTypeClothes().toString() + "'" +
                " and size=" + "'" + clothes.getSize().toString() + "'" +
                " and color=" + "'" + clothes.getColor().toString() + "'" +
                " and cotton=" + "'" + clothes.getCotton() + "'";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlFindInDB);

        if (!result.next()) {
            SqlRowSet sqlRowSetForInsert = jdbcTemplate.queryForRowSet("select * from transactions_rep order by id desc limit 1");
            if (sqlRowSetForInsert.next()) {
                idTr = sqlRowSetForInsert.getInt("id");
            }

            jdbcTemplate.update(
                    "insert into CLOTHES_REP (transactions_id, type_Clothes, size, color, cotton, quantity) values (?, ?, ?, ?, ?, ?)",
                    idTr,
                    clothes.getTypeClothes().toString(),
                    clothes.getSize().toString(),
                    clothes.getColor().toString(),
                    clothes.getCotton(),
                    clothes.getQuantity());
        } else {
            String sqlUpdate = "update CLOTHES_REP " +
                    "set quantity = " + (result.getInt("quantity") + clothes.getQuantity()) +
                    " where type_Clothes=" + "'" + clothes.getTypeClothes().toString() + "'" +
                    " and size=" + "'" + clothes.getSize().toString() + "'" +
                    " and color=" + "'" + clothes.getColor().toString() + "'" +
                    " and cotton=" + "'" + clothes.getCotton() + "'";

            jdbcTemplate.update(sqlUpdate);
        }
        return clothes;
    }

    @Override
    public int[] batchUpdate(final List<Clothes> clothes) {
        final int[] idTr = {0};
        SqlRowSet sqlRowSetForInsert = jdbcTemplate.queryForRowSet("select * from transactions_rep order by id desc limit 1");
        if (sqlRowSetForInsert.next()) {
            idTr[0] = sqlRowSetForInsert.getInt("id")-1;
        }
        int[] updateCounts = jdbcTemplate.batchUpdate(
                "insert into CLOTHES_REP (transactions_id, type_Clothes, size, color, cotton, quantity) values (?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        idTr[0]++;
                        ps.setInt(1, idTr[0]);
                        ps.setString(2, clothes.get(i).getTypeClothes().toString());
                        ps.setString(3, clothes.get(i).getSize().toString());
                        ps.setString(4, clothes.get(i).getColor().toString());
                        ps.setInt(5, clothes.get(i).getCotton());
                        ps.setInt(6, clothes.get(i).getQuantity());
                    }

                    @Override
                    public int getBatchSize() {
                        return clothes.size();
                    }
                }
        );
        return updateCounts;
    }

    private Clothes mapRowToClothes(ResultSet row, int rowNum)
            throws SQLException {

        return new Clothes(
                TypeClothes.valueOf(row.getString("type_clothes")),
                Size.valueOf(row.getString("size")),
                Color.valueOf(row.getString("color")),
                row.getInt("COTTON"),
                row.getInt("QUANTITY"));
    }
}
