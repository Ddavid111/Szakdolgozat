package com.example.proba.service;

import com.example.proba.dao.ReviewDao;
import com.example.proba.dao.ThesisDao;
import com.example.proba.dao.UserDao;
import com.example.proba.entity.Review;
import com.example.proba.entity.Thesis;
import com.example.proba.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            thesis = thesisFromSql;
            String thesisTitle = thesis.getTitle();


            String email = user.getEmail();
            String emailSubject = "Felkérés érkezett";
            String emailBody = "Önt felkérték egy szakdolgozat bírálatára.";
            emailBody += "\nA bírálandó szakdolgozat címe: " + thesisTitle;


            emailSenderService.sendEmail(email, emailSubject, emailBody);

            thesis.setUnderReview(true);
            thesisDao.save(thesis);
            reviewDao.save(review);
        }

        else {
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
