package com.storage.storagestoresocks.repository.impl;

import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class StorageRepositoryImplTest {
    private Connection connection;

    private StorageRepositoryImpl storageRepository;

    private Clothes clothes = new Clothes(TypeClothes.SOCKS, Size.SIZE_M, Color.BLUE, 80, 500);

    @Before
    public void setUp() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
        PreparedStatement createTestClothesTableStatement = connection.prepareStatement(
                "CREATE TABLE CLOTHES_REP (id integer AUTO_INCREMENT PRIMARY KEY, " +
                        "typeTransaction varchar not null," +
                        "    typeClothes         varchar not null," +
                        "    createTime        timestamp not null," +
                        "    clothesQuantity       integer not null)");
        createTestClothesTableStatement.executeUpdate();
        createTestClothesTableStatement.close();

        PreparedStatement createTestTransactionsTableStatement = connection.prepareStatement(
                "CREATE TABLE transactions_rep (id integer AUTO_INCREMENT PRIMARY KEY," +
                        "    transactions_id int not null references CLOTHES_REP(id)," +
                        "    type_Clothes varchar not null," +
                        "    size varchar not null," +
                        "    color varchar not null," +
                        "    cotton integer not null," +
                        "    quantity integer not null)");
        createTestTransactionsTableStatement.executeUpdate();
        createTestTransactionsTableStatement.close();

    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void testInsertAndSelect() throws SQLException {

        storageRepository.save(clothes);


        PreparedStatement selectStatement = connection.prepareStatement(
                "SELECT * FROM CLOTHES_REP WHERE id = ?");
        selectStatement.setInt(1, 1);
        ResultSet resultSet = selectStatement.executeQuery();
        assertNotNull(resultSet);
        resultSet.next();
//        assertEquals("John", resultSet.getString("name"));
        resultSet.close();
        selectStatement.close();
    }

}