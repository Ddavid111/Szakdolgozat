package com.example.proba.controller;

import com.example.proba.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/addFiles")
    public void addFiles(@RequestParam("file") MultipartFile file){
        fileService.uploadFile(file);

    }


}
