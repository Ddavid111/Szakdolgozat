package com.example.proba.service;

import com.example.proba.dao.ReviewDao;
import com.example.proba.dao.ThesesDao;
import com.example.proba.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private ThesesDao thesesDao;


    public Review addReviews(Review review)
    {

        return reviewDao.save(review);

    }

    /*public Integer findRoleIdByUserName(String userName) {

        List<Reviews> reviews = (List<Reviews>) reviewsDao.findAll();

        for (Reviews r : reviews) {
            if (r.getReviewerId().equals(userName)) {
                return r.getId();
            }

        }
        return-1;

    }*/

}