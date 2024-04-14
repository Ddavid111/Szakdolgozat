package com.example.proba.dao;

import com.example.proba.entity.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewDao extends CrudRepository<Review, Integer> {

    @Query(value= "SELECT * FROM Review WHERE thesis_id = :condition", nativeQuery = true)
    List<Review> findReviewByThesisId(@Param("condition") Integer thesisId);

    @Query(value= "SELECT * FROM Review WHERE thesis_id = :thesisId and reviewer_id = :reviewerId ", nativeQuery = true)
    List<Review> findReviewsByThesisIdAndReviewerId(@Param("thesisId") Integer thesisId, @Param("reviewerId") Integer reviewerId);

    @Query(value= "SELECT * FROM Review WHERE thesis_id = :thesisId and reviewer_id = :reviewerId limit 1", nativeQuery = true)
    Review findReviewByThesisIdAndReviewerId(@Param("thesisId") Integer thesisId, @Param("reviewerId") Integer reviewerId);

    @Query(value= "SELECT * FROM Review WHERE reviewer_id = :reviewerId ", nativeQuery = true)
    List<Review> findReviewsByReviewerId(@Param("reviewerId") Integer reviewerId);

    @Query(value= "SELECT * FROM Review r INNER JOIN Thesis t on r.thesis_id = t.id WHERE t.user_id = :userId and r.reviewer_id = :reviewerId ", nativeQuery = true)
    List<Review> findThesesByUserIdAndReviewerId(@Param("userId") Integer userId, @Param("reviewerId") Integer reviewerId);

    @Query(value = "SELECT count(thesis_id) FROM Review WHERE thesis_id = :thesisId", nativeQuery = true)
    Integer countByThesesesId(@Param("thesisId") Integer thesisId);

}
