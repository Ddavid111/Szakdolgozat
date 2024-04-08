package com.example.proba.controller;

import com.example.proba.entity.Thesis;
import com.example.proba.service.ReportStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportStatusController {

    @Autowired
    ReportStatusService reportStatusService;


    @PostMapping("/requestForReview")
    public void requestForReview(@RequestParam Integer userId, @RequestParam Integer thesisId) {
        reportStatusService.requestForReview(userId, thesisId);
    }

    @GetMapping("/findThesesUnderReview")
    public List<Thesis> findThesesUnderReview(@RequestParam Boolean isUnderReview)
    {
        return reportStatusService.findThesesUnderReview(isUnderReview);
    }

    @GetMapping("/findReviewedTheses")
    public List<Thesis> findReviewedTheses(){
        return reportStatusService.findReviewedTheses();

    }




}
