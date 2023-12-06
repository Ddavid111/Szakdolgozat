package com.example.proba.service;

import com.example.proba.dao.FileDao;
import com.example.proba.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class FileService {

    @Autowired
    private FileDao fileDao;

    public void uploadFile(MultipartFile file){
        File fileTest = new File();
        String filename = file.getOriginalFilename();

        fileTest.setUploadTime( new Date(System.currentTimeMillis()));
        try {

            if(file.getContentType().contains("presentation"))
            {
                Files.write(Paths.get("ppts/" + filename), file.getBytes());
                fileTest.setName("ppts/" + filename);
                fileDao.save(fileTest);
            }
            else if(file.getContentType().equals("application/pdf")) {
                Files.write(Paths.get("pdfs/" + filename), file.getBytes());
                fileTest.setName("pdfs/" + filename);
                fileDao.save(fileTest);
            }
            else if(file.getContentType().equals("application/zip") || file.getContentType().equals("application/x-zip-compressed")) {
                Files.write(Paths.get("attachments/" + filename), file.getBytes());
                fileTest.setName("attachments/" + filename);
                fileDao.save(fileTest);
            }
            else {
                throw new Exception("File format cannot be identified!");
            }
        }
        catch (IOException e) {
            e.printStackTrace();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
