package com.storage.storagestoresocks.controllers;

import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.Socks;
import com.storage.storagestoresocks.models.clothes.enums.*;
import com.storage.storagestoresocks.services.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/storage")
public class StorageController {

    StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping()
    public ResponseEntity<Clothes[]> addSocksInStorage(@Valid @RequestBody Clothes...clothes) throws QuantityException {
        for (Clothes clothe : clothes) {
         storageService.addSocksInStorage(clothe);
        }
        return ResponseEntity.ok(clothes);
    }

    @GetMapping("/allSocks")
    public ResponseEntity<List<Clothes>> getAllSocksList() {
        return ResponseEntity.ok(storageService.obtainAllSocks());
    }

    @GetMapping("/quantity")

    public ResponseEntity<Integer> obtainQuantity(Color color,
                                                  Size size,
                                                  int cottonMin,
                                                  int cottonMax) {

        int quantity = storageService.availabilityCheck(color, size, cottonMin, cottonMax);

        return ResponseEntity.ok(quantity);
    }

    @PutMapping
    public ResponseEntity<Object> getFromStock(@Valid @RequestBody Clothes clothes) {
        int a = 0;
        try {
            a = storageService.getFromStock(clothes);
        } catch (QuantityException e) {
            ResponseEntity.badRequest().body(e);
        }
        return ResponseEntity.ok().body("На складе осталось " + a);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteFromStock(@Valid @RequestBody Clothes clothes) {
        int a = 0;
        try {
            a = storageService.deleteFromStock(clothes);
        } catch (QuantityException e) {
            ResponseEntity.badRequest().body(e);
        }
        return ResponseEntity.ok().body("На складе осталось " + a);
    }
}
