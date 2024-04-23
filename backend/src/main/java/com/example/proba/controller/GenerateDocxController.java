package com.example.proba.controller;

import com.example.proba.service.FileService;
import com.example.proba.service.GenerateDocxService;
import com.example.proba.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class GenerateDocxController {

    @Autowired
    GenerateDocxService generateDocxService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    private FileService fileService;

    @PostMapping("/generateDocx")
    public ResponseEntity<FileSystemResource> generateDocx(@RequestBody Object docxData)  {
        System.out.println(docxData);
        UUID uuid = generateDocxService.Proba2(docxData);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileService.getFilenameByUUID(uuid));
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileService.getFile(fileService.getFilenameByUUID(uuid)));

    }










}
