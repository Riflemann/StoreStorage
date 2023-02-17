package com.storage.storagestoresocks.controllers;

import com.storage.storagestoresocks.models.Socks;
import com.storage.storagestoresocks.models.Transaction;
import com.storage.storagestoresocks.services.TransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionsController {

    TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @GetMapping("/allTransactions")
    public ResponseEntity<List<Transaction>> getAllSocksList() {
        return ResponseEntity.ok(transactionsService.getAllTransactions());
    }


}
