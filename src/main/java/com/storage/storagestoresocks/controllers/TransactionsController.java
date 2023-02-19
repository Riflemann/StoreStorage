package com.storage.storagestoresocks.controllers;

import com.storage.storagestoresocks.models.Transaction;
import com.storage.storagestoresocks.models.clothes.enums.Color;
import com.storage.storagestoresocks.models.clothes.enums.Size;
import com.storage.storagestoresocks.models.clothes.enums.TypeClothes;
import com.storage.storagestoresocks.services.TransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return ResponseEntity.ok().body(transactionsService.getAllTransactions());
    }

    @GetMapping("/quantity")
    public ResponseEntity<Integer> obtainQuantity(@RequestParam(required = false) Color color,
                                                  @RequestParam(required = false) Size size,
                                                  @RequestParam(required = false) TypeClothes typeClothes,
                                                  @RequestParam(required = false) String fromDate,
                                                  @RequestParam(required = false) String toDate,
                                                  int cottonMin,
                                                  int cottonMax) {

        int quantity = transactionsService.availabilityCheck(color, size, typeClothes, fromDate, toDate, cottonMin, cottonMax);

        return ResponseEntity.ok(quantity);
    }


}
