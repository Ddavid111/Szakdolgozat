package com.example.proba.controller;

import com.example.proba.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/addFiles")
    public int addFiles(@RequestParam("file") MultipartFile file) { // , @RequestParam("thesesId") Integer thesesId
        return fileService.uploadFile(file); // returns with the fileId in the File table
    }


    @PostMapping("/assembleFileWithStudent")
    public void assembleFileWithStudent(@RequestParam int fileId, @RequestParam int thesisId) {
        fileService.assembleFileWithStudent(fileId, thesisId);
    }

    @DeleteMapping("/deleteFile")
    public void deleteFile(@RequestParam String uuid){
        fileService.deleteFileById(uuid);
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam UUID uuid) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileService.getFilenameByUUID(uuid));
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileService.getFile(fileService.getFilenameByUUID(uuid)));
    }
}
