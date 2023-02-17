package com.storage.storagestoresocks.controllers;

import com.storage.storagestoresocks.services.FileService;
import com.storage.storagestoresocks.services.StorageService;
import com.storage.storagestoresocks.services.TransactionsService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/files")
public class FilesController {

    @Value("${name.of.storage.file}")
    private String storageFileName;

    @Value("${name.of.transaction.file}")
    private String transactionFileName;

    @Value("${path.to.file.folder}")
    private String filePath;

    private FileService fileService;
    private StorageService storageService;
    private TransactionsService transactionsService;

    public FilesController(FileService fileService, StorageService storageService, TransactionsService transactionsService) {
        this.fileService = fileService;
        this.storageService = storageService;
        this.transactionsService = transactionsService;
    }

    @GetMapping(value = "/storage")
    public ResponseEntity<InputStreamResource> downloadStorageFile() throws FileNotFoundException {

        File file = new File(filePath + "/" + storageFileName);

        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Storage.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/transactions")
    public ResponseEntity<InputStreamResource> downloadTransactionsFile() throws FileNotFoundException {

        File file = new File(filePath + "/" + transactionFileName);

        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Transactions.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/storage/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadStorage(@RequestParam MultipartFile file) {
        fileService.cleanFile(storageFileName);
        File storageFile = new File(filePath + "/" + storageFileName);

        try(FileOutputStream fos = new FileOutputStream(storageFile)) {

            IOUtils.copy(file.getInputStream(), fos);
            return ResponseEntity.ok().build();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping(value = "/transactions/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadTransactions(@RequestParam MultipartFile file) {
        fileService.cleanFile(transactionFileName);
        File storageFile = new File(filePath + "/" + transactionFileName);

        try(FileOutputStream fos = new FileOutputStream(storageFile)) {

            IOUtils.copy(file.getInputStream(), fos);
            return ResponseEntity.ok().build();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/getStorageTxtFile")
    public ResponseEntity<Object> getTxtFile() {
        try {
            Path path = fileService.createTxtFile(storageService.obtainMapAllSocks());
            if (Files.size(path) == 0) {
                return ResponseEntity.noContent().build();
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .contentLength(Files.size(path))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"recipeBook.txt\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.toString());
        }

    }
}
