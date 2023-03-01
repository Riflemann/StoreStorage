package com.storage.storagestoresocks.repository.impl;

import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import com.storage.storagestoresocks.repository.StorageRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class StorageRepositoryImpl implements StorageRepository {
    private JdbcTemplate jdbcTemplate;

    public StorageRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Clothes> findAll() {
        List<Clothes> list = null;
        try {
            list = jdbcTemplate.query(
                    "select * from CLOTHES_REP",
                    this::mapRowToClothes);
        } catch (DataAccessException e) {
            System.out.println(e.toString());
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
        jdbcTemplate.update(
                "insert into CLOTHES_REP (type_Clothes, size, color, cotton, quantity) values (?, ?, ?, ?, ?)",
                clothes.getTypeClothes().toString(),
                clothes.getSize().toString(),
                clothes.getColor().toString(),
                clothes.getCotton(),
                clothes.getQuantity());
        return clothes;
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
