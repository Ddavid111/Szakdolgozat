package com.example.proba.service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.List;


@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithAttachments(String toEmail, String subject, String body, List<String> attachmentPaths) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("mintakalman1@gmail.com");
            helper.setTo(toEmail);
            helper.setText(body);
            helper.setSubject(subject);

            for (String pathToAttachment : attachmentPaths) {
                FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
                helper.addAttachment(file.getFilename(), file);
            }

        } catch (Exception e) {
            System.out.println("Exception when creating email");
            System.out.println(e.getMessage());
        }

        mailSender.send(message);
        System.out.println("Mail sent successfully...");
    }

    public void sendEmail(String toEmail, String subject, String body) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("mintakalman1@gmail.com");
            helper.setTo(toEmail);
            helper.setText(body);
            helper.setSubject(subject);

        } catch (Exception e) {
            System.out.println("Exception when creating email");
            System.out.println(e.getMessage());
        }


        mailSender.send(message);
        System.out.println("Mail sent succesfully...");

    }

}



