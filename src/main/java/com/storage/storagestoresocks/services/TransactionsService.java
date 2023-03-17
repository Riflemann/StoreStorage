package com.storage.storagestoresocks.services;

import com.storage.storagestoresocks.models.Transaction;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import com.storage.storagestoresocks.models.clothes.enums.TypeTransaction;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.List;

public interface TransactionsService {
    @PostConstruct
    void init();


//    void addTransactions(TypeTransaction typeTransaction, TypeClothes typeClothes,
//                         int socksQuantity, int cotton);

    List<Transaction> getAllTransactions();

//    List<Transaction> extractList(@RequestParam(required = false) String fromDate,
//                     @RequestParam(required = false) String toDate,
//                     int cottonMin,
//                     int cottonMax);
//
//    int calculateQuantity(@RequestParam(required = false) Color color,
//                          @RequestParam(required = false) Size size,
//                          @RequestParam(required = false) TypeClothes typeClothes);
}
