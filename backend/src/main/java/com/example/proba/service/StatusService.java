package com.example.proba.service;

import com.example.proba.dao.FileDao;
import com.example.proba.dao.UserDao;
import com.example.proba.entity.File;
import com.example.proba.entity.Thesis;
import com.example.proba.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.example.proba.util.Triple;

import javax.mail.MessagingException;

@Service
public class StatusService {

    @Autowired
    FileDao fileDao;

    @Autowired
    ThesesService thesesService;

    @Autowired
    UserService userService;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    UserDao userDao;

    public List<Triple<Integer, String, String>> getNeccessaryData() {
        List<Triple<Integer, String, String>> fileData = new ArrayList<>();

        // lejön a query
        List<File> tempData = fileDao.findByTheses();

        for (File f : tempData) {
            System.out.println(f.getThesis().getId() + "\t" + f.getName() + "\t" + f.getUuid());
            fileData.add(new Triple<>(f.getThesis().getId(), f.getName(), f.getUuid()));
        }

        return fileData;
    }

    public void findFilesByThesesId(Integer thesisId) throws MessagingException {
        Thesis thesisFromSql = thesesService.findThesesById(thesisId);
        if (thesisFromSql != null) {
            Integer userId = thesisFromSql.getUserId();
            User user = userService.findUserById(userId);
            String email = user.getEmail();
            String fullname = user.getFullname();

            // Bírálatok keresése az adott thesisId alapján
            List<File> files = fileDao.findFilesByThesesId(thesisId);
            List<String> attachmentPaths = new ArrayList<>();

            if (!files.isEmpty()) {
                String emailSubject = "Bírálat jegyzőkönyv";
                String emailBody = "Kedves" + fullname;
                emailBody += "\nMegérkezett a bírálat jegyzőkönyve amit a mellékletben talál.";
                emailBody += "\n";
                emailBody += "\nÜdvözlettel: Adminisztrátor";

                // Az összes bírálat mellékelése az emailhez
                for (File file : files) {
                    String filename = file.getName();
                    if (filename.startsWith("reviews")) {
                        attachmentPaths.add(filename);
                    }
                }

                emailSenderService.sendEmailWithAttachments(email, emailSubject, emailBody, attachmentPaths);


                System.out.println("Bírálatok elküldve az emailben.");
            } else {
                System.out.println("Nincs bírálat az adott thesishez.");
            }
        }
    }
}