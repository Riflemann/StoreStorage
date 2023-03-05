package com.storage.storagestoresocks.controllers;

import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.*;
import com.storage.storagestoresocks.repository.StorageRepository;
import com.storage.storagestoresocks.services.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/storage")
public class StorageController {

    StorageService storageService;

    StorageRepository storageRepository;

    public StorageController(StorageService storageService, StorageRepository storageRepository) {
        this.storageService = storageService;
        this.storageRepository = storageRepository;
    }

    @PostMapping()
    public ResponseEntity<Clothes[]> addSocksInStorage(@Valid @RequestBody Clothes... clothes) throws QuantityException {
        for (Clothes clothe : clothes) {
            storageService.addClothesInStorage(clothe);
        }
        return ResponseEntity.ok(clothes);
    }

    @GetMapping("/allSocks")
    public ResponseEntity<List<Clothes>> getAllSocksList() {
        return ResponseEntity.ok(storageService.obtainAllClothes());
    }

    @GetMapping("/from/h2")
    public ResponseEntity<List<Clothes>> getAllFromH2() {
        return ResponseEntity.ok(storageRepository.findAll());
    }

    @GetMapping("/from/h2byId")
    public ResponseEntity<Optional<Clothes>> getAllFromH2ById(String id) {
        return ResponseEntity.ok(storageRepository.findById(id));
    }

    @GetMapping("/save")
    public ResponseEntity<Clothes> saveToH2(Clothes clothes) {
        return ResponseEntity.ok(storageRepository.save(clothes));
    }
    @GetMapping("/update")
    public ResponseEntity<Clothes> updateInH2(Clothes clothes) {
        return ResponseEntity.ok(storageRepository.updateClothes(clothes));
    }


    @GetMapping("/quantity")

    public ResponseEntity<Integer> obtainQuantity(@RequestParam(required = false) Color color,
                                                  @RequestParam(required = false) Size size,
                                                  @RequestParam(required = false) TypeClothes typeClothes,
                                                  int cottonMin,
                                                  int cottonMax) {

        int quantity = storageService.availabilityCheck(color, size, typeClothes, cottonMin, cottonMax);

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
