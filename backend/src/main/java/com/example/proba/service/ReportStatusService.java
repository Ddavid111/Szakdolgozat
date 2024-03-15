package com.example.proba.service;

import com.example.proba.dao.ReviewDao;
import com.example.proba.dao.ThesesDao;
import com.example.proba.dao.UserDao;
import com.example.proba.entity.Review;
import com.example.proba.entity.Theses;
import com.example.proba.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReportStatusService {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    UserService userService;

    @Autowired
    ThesesService thesesService;

    @Autowired
    ThesesDao thesesDao;

    @Autowired
    ReviewDao reviewDao;

    @Autowired
    UserDao userDao;


    public void requestForReview(Integer userId, Integer thesisId) {
        User userFromSql = userService.findUserById(userId);
        User user;

        Optional<Theses> thesesFromSql = thesesService.findThesesById(thesisId);
        Theses theses;

        Review review = new Review();

        review.setInvitationDate(new Date());
        review.setUser(userDao.findById(userId).get());
        review.setTheseses(thesesDao.findById(thesisId).get());

        if(thesesFromSql.isPresent()) {
            user = userFromSql;
            theses = thesesFromSql.get();
            String thesisTitle = theses.getTitle();


            String email = user.getEmail();
            String emailSubject = "Felkérés érkezett";
            String emailBody = "Önt felkérték egy szakdolgozat bírálatára.";
            emailBody += "\nA bírálandó szakdolgozat címe: " + thesisTitle;


            emailSenderService.sendEmail(email, emailSubject, emailBody);

            theses.setUnderReview(true);
            thesesDao.save(theses);
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

    public List<Theses> findThesesUnderReview (Boolean isUnderReview)
    {
        Iterable<Theses> thesesIterable = thesesDao.findThesesUnderReview(isUnderReview);
        List<Theses> theses = new ArrayList<>();

        thesesIterable.forEach(theses::add);

        return theses;


    }

    public List<Theses> findReviewedTheses ()
    {
        Iterable<Theses> thesesIterable = thesesDao.findReviewedTheses();
        List<Theses> theses = new ArrayList<>();

        thesesIterable.forEach(theses::add);

        return theses;


    }



}
