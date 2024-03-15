package com.example.proba.dao;

import com.example.proba.entity.Theses;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThesesDao extends CrudRepository<Theses, Integer> {

    @Query(value = "SELECT * from Theses where user_id = :condition", nativeQuery = true)
    List<Theses> findByUserId(@Param("condition") Integer userId);

    @Query(value = "SELECT id, user_id, title, faculty, department, speciality, language, " +
            "has_msc_apply, submission_date, answer, supervisor_id," +
            "consultant_id, topic_score, null as subject_score, " +
            "null as defense_score, null as final_score, null as is_under_review, null as reviews_remaining, session_id, topics_id, supplement_id, thesis_pdf_id FROM Theses", nativeQuery = true)
    List<Theses> getThesesListToDisplay();
    @Query(value = "SELECT count(id) FROM Theses ", nativeQuery = true)
    Integer getCountOfRecords();

    @Query(value = "SELECT id, user_id, title, supervisor_id, submission_date, null as final_score, " +
            "null as session_id, null as topics_id, null as supplement_id, null as thesis_pdf_id, null as answer, null as consultant_id, null as defense_score, null as department, null as faculty, " +
            "null as  has_msc_apply, null as language, null as speciality,null as subject_score," +
            "null as thesis_pdf_id, null as supplement_id, null as topic_score,null as session_id, null as topics_id, null as is_under_review, null as reviews_remaining" +
            " FROM Theses where is_under_review = :condition", nativeQuery = true)
    List<Theses> findThesesUnderReview(@Param("condition") Boolean isUnderReview);


    @Query(value = "SELECT id, user_id, title, supervisor_id, submission_date, null as final_score," +
            "null as session_id, null as topics_id, null as supplement_id, null as thesis_pdf_id, null as answer, null as consultant_id, null as defense_score, null as department, null as faculty," +
            "null as  has_msc_apply, null as language, null as speciality,null as subject_score, "+
            "null as thesis_pdf_id, null as supplement_id, null as topic_score,null as session_id, null as topics_id, null as is_under_review, reviews_remaining "+
            " FROM Theses where  reviews_remaining <= 0" , nativeQuery = true)
    List<Theses> findReviewedTheses();




}
