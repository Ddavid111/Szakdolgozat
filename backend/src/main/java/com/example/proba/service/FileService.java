package com.example.proba.service;

import com.example.proba.dao.FileDao;
import com.example.proba.dao.ThesisDao;
import com.example.proba.dao.UserDao;
import com.example.proba.entity.*;
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
    private ThesisDao thesisDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ThesesService thesesService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void assembleFileWithStudent(int fileId, int thesisId) {
        Optional<File> file = fileDao.findById(fileId);
        Optional<Thesis> theses = thesisDao.findById(thesisId);

        if(file.isPresent() && theses.isPresent()) {
            File f = file.get();
            Thesis t = theses.get();
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

//    public List<Thesis> findThesesByLoggedInStudent (Integer userId)
//    {
//        User user = userService.findUserById(userId);
//        Role role = user.getRole();
//        List<Thesis> thesis = thesesService.findThesesByUserId(userId);
//        switch(role) {
//            case Hallgató: {
//                List<Thesis> theseses = new ArrayList<>();
//                for (Thesis theses : thesis) {
//                    Integer id = theses.getId();
//                    List<File> file = fileDao.findFilesByThesesId(id);
//                    if(file.isEmpty())
//                    {
//                        theseses.add(theses);
//                    }
//                    else if (file.size() < 3){
//                        for (File files : file) {
//                            if (files.getName().startsWith("attachments") && !files.getName().startsWith("pdfs")) {
//                                {
//                                    theseses.add(theses);
//                                }
//                            }
//                            else if (!files.getName().startsWith("attachments") && files.getName().startsWith("pdfs")) {
//                                theseses.add(theses);
//                            }
//                            else if (files.getName().startsWith("attachments") && files.getName().contains("pdfs")) {
//                                theseses.clear();
//                            }
//                        }
//                    }
//                    return theseses;
//                }
//            }
//                default: {
//                    return null;
//                }
//        }
//    }

    public List<Thesis> findThesesByLoggedInStudent(Integer userId) {
        User user = userService.findUserById(userId);
        Role role = user.getRole();
        List<Thesis> theses = thesesService.findThesesByUserId(userId);

        switch (role) {
            case Hallgató: {
                List<Thesis> theseses = new ArrayList<>();
                for (Thesis thesis : theses) {
                    Integer thesisId = thesis.getId();
                    List<File> files = fileDao.findFilesByThesesId(thesisId);
                    boolean attachmentFound = false;
                    boolean pdfFound = false;
                    boolean pptFound = false;

                    for (File file : files) {
                        if (file.getName().startsWith("attachments")) {
                            attachmentFound = true;
                        } else if (file.getName().startsWith("pdfs")) {
                            pdfFound = true;
                        } else if (file.getName().startsWith("ppts")) {
                            pptFound = true;
                        }
                    }

                    if ((attachmentFound && pdfFound && !pptFound) ||
                            (attachmentFound && !pdfFound && pptFound) ||
                            (!attachmentFound && pdfFound && pptFound) ||
                            (!attachmentFound && !pdfFound && pptFound)) {
                        theseses.add(thesis);
                    } else if (files.isEmpty() ||
                            (attachmentFound && !pdfFound && !pptFound) ||
                            (!attachmentFound && pdfFound && !pptFound) ||
                            (!attachmentFound && !pdfFound && !pptFound)) {
                        theseses.add(thesis);
                    }

                }
                return theseses;
            }
            default: {
                return null;
            }
        }
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
