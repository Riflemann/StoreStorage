package com.storage.storagestoresocks.controllers;

import com.storage.storagestoresocks.models.Transaction;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import com.storage.storagestoresocks.repository.TransactionsRepository;
import com.storage.storagestoresocks.services.TransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionsController {

    TransactionsService transactionsService;

    TransactionsRepository transactionsRepository;

    public TransactionsController(TransactionsService transactionsService, TransactionsRepository transactionsRepository) {
        this.transactionsService = transactionsService;
        this.transactionsRepository = transactionsRepository;
    }

    @GetMapping("/from/h2")
    public ResponseEntity<List<Transaction>> getAllFromH2() {
        return ResponseEntity.ok(transactionsRepository.findAll());
    }

    @GetMapping("/save")
    public ResponseEntity<Transaction> saveToH2(Transaction transaction) {
        return ResponseEntity.ok(transactionsRepository.save(transaction));
    }

    @GetMapping("/allTransactions")
    public ResponseEntity<List<Transaction>> getAllSocksList() {
        return ResponseEntity.ok().body(transactionsService.getAllTransactions());
    }

    @GetMapping("/quantity")
    public ResponseEntity<String> obtainQuantity(@RequestParam(required = false) Color color,
                                                  @RequestParam(required = false) Size size,
                                                  @RequestParam(required = false) TypeClothes typeClothes,
                                                  @RequestParam(required = false) String fromDate,
                                                  @RequestParam(required = false) String toDate,
                                                  int cottonMin,
                                                  int cottonMax) {

        List<Transaction> transactions = new ArrayList<>(transactionsService.extractList(fromDate, toDate, cottonMin, cottonMax));
        int quantity = transactionsService.calculateQuantity(color, size, typeClothes);

        return ResponseEntity.ok().body("На складе найдено: " + quantity + "шт.\n" + transactions);
    }


}
