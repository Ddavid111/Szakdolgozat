package com.example.proba.service;

import com.example.proba.dao.ReviewDao;
import com.example.proba.dao.ThesisDao;
import com.example.proba.dao.UserDao;
import com.example.proba.entity.Review;
import com.example.proba.entity.Thesis;
import com.example.proba.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportStatusService {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    UserService userService;

    @Autowired
    ThesesService thesesService;

    @Autowired
    ThesisDao thesisDao;

    @Autowired
    ReviewDao reviewDao;

    @Autowired
    UserDao userDao;


    public void requestForReview(Integer userId, Integer thesisId) {
        User userFromSql = userService.findUserById(userId);
        User user;

        Thesis thesisFromSql = thesesService.findThesesById(thesisId);
        Thesis thesis;

        Review review = new Review();

        review.setInvitationDate(new Date());
        review.setUser(userDao.findById(userId).get());
        review.setTheseses(thesisDao.findById(thesisId).get());

        if(thesisFromSql != null) {
            user = userFromSql;
            String fullname = user.getFullname();
            thesis = thesisFromSql;
            String thesisTitle = thesis.getTitle();

            try {
                // Fájl beolvasása
                BufferedReader reader = new BufferedReader(new FileReader("email/request_review.txt"));
                StringBuilder emailBodyBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {

                    if (line.contains("[Szakdolgozat név]")) {
                        line = line.replace("[Szakdolgozat név]", thesisTitle);
                    }

                    if (line.contains("[Név]")) {
                        line = line.replace("[Név]", fullname);
                    }
                    emailBodyBuilder.append(line).append("\n");
                }
                reader.close();
                String emailBody = emailBodyBuilder.toString();

                // Email elküldése
                String email = user.getEmail();
                String emailSubject = "Felkérés érkezett";
                emailSenderService.sendEmail(email, emailSubject, emailBody);

                thesis.setUnderReview(true);
                thesisDao.save(thesis);
                reviewDao.save(review);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                throw new Exception("Reviewer user or the thesis to be reviewed was not found in the DB!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Thesis> findThesesUnderReview (Boolean isUnderReview)
    {
        Iterable<Thesis> thesesIterable = thesisDao.findThesesUnderReview(isUnderReview);
        List<Thesis> theses = new ArrayList<>();

        thesesIterable.forEach(theses::add);

        return theses;


    }

    public List<Thesis> findReviewedTheses ()
    {
        Iterable<Thesis> thesesIterable = thesisDao.findReviewedTheses();
        List<Thesis> theses = new ArrayList<>();

        thesesIterable.forEach(theses::add);

        return theses;


    }



}
