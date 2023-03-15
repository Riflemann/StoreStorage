package com.storage.storagestoresocks.controllers;

import com.storage.storagestoresocks.exceptions.NotFoundException;
import com.storage.storagestoresocks.exceptions.QuantityException;
import com.storage.storagestoresocks.models.clothes.Clothes;
import com.storage.storagestoresocks.models.clothes.enums.*;
import com.storage.storagestoresocks.repository.StorageRepository;
import com.storage.storagestoresocks.services.StorageService;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<Clothes[]> addClothesInStorage(@Valid @RequestBody Clothes... clothes) throws QuantityException {
        for (Clothes clothe : clothes) {
            storageService.addClothesInStorage(clothe);
        }
        return ResponseEntity.ok(clothes);
    }

    @GetMapping("/allSocks")
    public ResponseEntity<List<Clothes>> getAllSocksList() {
        return ResponseEntity.ok(storageService.obtainAllClothes());
    }

    @PutMapping("/AsyncStorage")
    public ResponseEntity<Clothes> getFromStorage(@Valid @RequestBody Clothes clothes) {

        try {
            storageRepository.obtainFromStorage(clothes);
        } catch (QuantityException | NotFoundException e) {
            ResponseEntity.badRequest().body(e);
        }
       return ResponseEntity.ok().body(clothes);
    }

    @GetMapping("/batch_update_test")
    public ResponseEntity<int[]> batchUpdateTest() {

        List<Clothes> clothesList = new ArrayList<>();
        TypeClothes[] typeClothes = TypeClothes.values();
        Size[] sizes = Size.values();
        Color[] colors = Color.values();
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        for (int i = 0; i < 10000; i++) {

            clothesList.add(new Clothes(
                    typeClothes[(int) (Math.random() * 4)],
                    sizes[(int) (Math.random() * 3)],
                    colors[(int) (Math.random() * 3)],
                    (int) (Math.random() * 100),
                    (int) (Math.random() * 600)
            ));
        }
        stopWatch.stop();
        System.out.println("Time has passed with create objects, ms: " + stopWatch.getTime());

        stopWatch.reset();
        stopWatch.start();
        int[] quantityOfUpdating = storageRepository.batchUpdate(clothesList);
        stopWatch.stop();
        System.out.println("Time has passed with insert 10000 issue, ms: " + stopWatch.getTime());

        return ResponseEntity.ok().body(quantityOfUpdating);
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
