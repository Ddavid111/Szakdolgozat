package com.example.proba.controller;

import com.example.proba.entity.Review;
import com.example.proba.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewsController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/addReviews")
    public Review addReviews(@RequestBody Review review){

        return reviewService.addReviews(review);


    }




}
