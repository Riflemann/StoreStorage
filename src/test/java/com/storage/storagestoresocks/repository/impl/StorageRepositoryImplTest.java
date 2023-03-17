package com.storage.storagestoresocks.repository.impl;

import com.storage.storagestoresocks.exceptions.NotFoundException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import com.storage.storagestoresocks.repository.TransactionsRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
@ExtendWith(MockitoExtension.class)
class StorageRepositoryImplTest {
    private static Connection connection;

    @Mock
    private static JdbcTemplate jdbcTemplate;

    @Mock
    private static TransactionsRepository transactionsRepository;

    @InjectMocks
    private static StorageRepositoryImpl out;

    private Clothes clothes = new Clothes(TypeClothes.SOCKS, Size.SIZE_M, Color.BLUE, 80, 500);

    @BeforeAll
    static void setUp() throws Exception {
//        jdbcTemplate = mock(JdbcTemplate.class);
//        transactionsRepository = mock(TransactionsRepositoryImpl.class);
//        out = new StorageRepositoryImpl(jdbcTemplate, transactionsRepository);

        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");

        PreparedStatement createTestClothesTableStatement = connection.prepareStatement(
                "CREATE TABLE transactions_rep (id integer AUTO_INCREMENT PRIMARY KEY, " +
                        "typeTransaction varchar not null," +
                        "    typeClothes         varchar not null," +
                        "    createTime        timestamp not null," +
                        "    clothesQuantity       integer not null)");
        createTestClothesTableStatement.executeUpdate();
        createTestClothesTableStatement.close();

        PreparedStatement createTestTransactionsTableStatement = connection.prepareStatement(
                "CREATE TABLE CLOTHES_REP (id integer AUTO_INCREMENT PRIMARY KEY," +
                        "    transactions_id int not null references CLOTHES_REP(id)," +
                        "    type_Clothes varchar not null," +
                        "    size varchar not null," +
                        "    color varchar not null," +
                        "    cotton integer not null," +
                        "    quantity integer not null)");
        createTestTransactionsTableStatement.executeUpdate();
        createTestTransactionsTableStatement.close();

        PreparedStatement insertTransactionInTableStatement = connection.prepareStatement(
                "INSERT INTO transactions_rep (typeTransaction, typeClothes, createTime, clothesQuantity) VALUES ('INCOMING', 'SOCKS', '2023-03-02',1500)");
        insertTransactionInTableStatement.executeUpdate();
        insertTransactionInTableStatement.close();

        PreparedStatement insertClothesInTableStatement = connection.prepareStatement(
                "INSERT INTO CLOTHES_REP (transactions_id, type_Clothes, size, color, cotton, quantity) VALUES (1, 'SOCKS', 'SIZE_M', 'BLUE', 50,1500)");
        insertClothesInTableStatement.executeUpdate();
        insertClothesInTableStatement.close();

    }

    @AfterAll
    static void tear() throws Exception {
        connection.close();
    }

    @Test
    public void testSaveMethod() throws SQLException {

        SqlRowSet sqlRowSet = mock(SqlRowSet.class);

        when(jdbcTemplate.queryForRowSet(startsWith("select * from CLOTHES_REP"))).thenReturn(sqlRowSet);
        when(sqlRowSet.next()).thenReturn(true);

        out.save(clothes);

        PreparedStatement selectStatement = connection.prepareStatement(
                "SELECT * FROM CLOTHES_REP WHERE id = ?");
        selectStatement.setInt(1, 2);
        ResultSet resultSet = selectStatement.executeQuery();
        assertNotNull(resultSet);
        resultSet.next();
        resultSet.close();
        selectStatement.close();
    }

}