package com.example.proba.controller;

import com.example.proba.entity.Review;
import com.example.proba.entity.Thesis;
import com.example.proba.service.FileService;
import com.example.proba.service.ReviewService;
import com.example.proba.service.StatusService;
import com.example.proba.util.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import java.util.List;
import java.util.UUID;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private FileService fileService;

    @PostMapping("/addReviews")
    public Review addReview(@RequestBody Review reviewData) throws Exception {
        System.out.println(reviewData.toString());
        return reviewService.addReview(reviewData);
    }

    @GetMapping("/getFileList")
    public List<Triple<Integer, String, String>> getTestList() {
        return statusService.getNeccessaryData();
    }

    @GetMapping("/getReviewData")
    public List<Review> getReviewData() {
        return reviewService.getReviewData();
    }

    @PostMapping("/generateReview")
    public ResponseEntity<FileSystemResource> generateReview(@RequestBody Object docxData) {
        System.out.println(docxData);
        UUID uuid = reviewService.Proba(docxData);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileService.getFilenameByUUID(uuid));
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileService.getFile(fileService.getFilenameByUUID(uuid)));
    }

    @GetMapping("/findThesesByUserIdAndReviewerId")
    public List<Thesis> findThesesByUserIdAndReviewerId(Integer userId, Integer reviewerId)
    {
        return reviewService.findThesesByUserIdAndReviewerId(userId, reviewerId);
    }

    @GetMapping("/findFilesByThesesId")
    public void findFilesByThesesId(@RequestParam Integer thesisId) throws MessagingException {
        statusService.findFilesByThesesId(thesisId);
    }

}
