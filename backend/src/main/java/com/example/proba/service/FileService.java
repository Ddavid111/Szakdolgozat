package com.example.proba.service;

import com.example.proba.dao.FileDao;
import com.example.proba.dao.ThesesDao;
import com.example.proba.entity.File;
import com.example.proba.entity.Theses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileService {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private ThesesDao thesesDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void assembleFileWithStudent(int fileId, int thesisId) {
        Optional<File> file = fileDao.findById(fileId);
        Optional<Theses> theses = thesesDao.findById(thesisId);

        if(file.isPresent() && theses.isPresent()) {
            File f = file.get();
            Theses t = theses.get();
            f.setThesis(t);
            fileDao.save(f);
        }

         else {
            try {
                throw new Exception("Thesis not found!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getFilenameByUUID(UUID uuid) {
        String query = "select name from file where uuid='"+ uuid.toString() + "'";
        var resultSet = jdbcTemplate.queryForList(query);
        List<Object> result = new ArrayList<>();
        resultSet.forEach((key) -> {
            result.add(key.values());
        });


        String filename = result.get(0).toString();
        filename = filename.replace("[", "");
        filename = filename.replace("]", "");
        System.out.println("Filename: " + filename);
        return filename;
    }



    public FileSystemResource getFile(String filename) {
        Path filePath = Paths.get(filename);

        FileSystemResource resource;
        try {
            resource = new FileSystemResource(filePath);
            if(resource.exists()) {
                return resource;
            }

            else {
                throw new Exception("File not fould.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getFilenameTwoByUUID(String uuid) {
        String query = "select name from file where uuid='"+ uuid + "'";
        var resultSet = jdbcTemplate.queryForList(query);
        List<Object> result = new ArrayList<>();
        resultSet.forEach((key) -> {
            result.add(key.values());
        });


        String filename = result.get(0).toString();
        filename = filename.replace("[", "");
        filename = filename.replace("]", "");
        System.out.println("Filename: " + filename);
        return filename;
    }


    public void deleteFileById(String uuid){

        String filename = getFilenameTwoByUUID(uuid);

        java.io.File fileToDelete = new java.io.File(filename);

        if (fileToDelete.delete()) {
            System.out.println("A fájl sikeresen törölve.");
        } else {
            System.out.println("A fájl törlése sikertelen.");
        }

        String fileQuery = "delete from file where uuid = '" + uuid + "'";

        jdbcTemplate.update(fileQuery);
    }


    public int uploadFile(MultipartFile file){
        File fileTest = new File();
        String filename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        fileTest.setUploadTime(new Date(System.currentTimeMillis()));
        fileTest.setUuid(uuid);

        try {

            if(file.getContentType().contains("presentation"))
            {
                Files.write(Paths.get("ppts/" + uuid + "_"  + filename), file.getBytes());
                fileTest.setName("ppts/" + uuid + "_"  + filename);
                return fileDao.save(fileTest).getId();
            }
            else if(file.getContentType().equals("application/pdf")) {
                Files.write(Paths.get("pdfs/" + uuid + "_"  + filename), file.getBytes());
                fileTest.setName("pdfs/" + uuid + "_"  + filename);
                return fileDao.save(fileTest).getId();
            }
            else if(file.getContentType().equals("application/zip") || file.getContentType().equals("application/x-zip-compressed")) {
                Files.write(Paths.get("attachments/" + uuid + "_"  + filename), file.getBytes());
                fileTest.setName("attachments/" + uuid + "_"  + filename);
                return fileDao.save(fileTest).getId();
            }
            else {
                throw new Exception("File format cannot be identified!");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return -1;

        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }


}
