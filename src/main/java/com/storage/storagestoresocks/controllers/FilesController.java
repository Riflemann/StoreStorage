package com.storage.storagestoresocks.controllers;

import com.storage.storagestoresocks.services.FileService;
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

    public FilesController(FileService fileService) {
        this.fileService = fileService;
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
}
