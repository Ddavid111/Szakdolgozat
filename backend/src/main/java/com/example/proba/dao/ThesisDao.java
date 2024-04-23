package com.example.proba.dao;

import com.example.proba.entity.Thesis;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThesisDao extends CrudRepository<Thesis, Integer> {

    @Query(value = "SELECT * from Thesis where user_id = :condition", nativeQuery = true)
    List<Thesis> findThesesByUserId(@Param("condition") Integer userId);

    @Query(value = "SELECT id, user_id, title, faculty, department, speciality, language, " +
            "has_msc_apply, submission_date, answer, supervisor_id," +
            "consultant_id, topic_score," +
            "null as is_under_review, null as reviews_remaining, session_id, topic_id FROM Thesis", nativeQuery = true)
    List<Thesis> getThesesListToDisplay();

    @Query(value = "SELECT count(id) FROM Thesis ", nativeQuery = true)
    Integer getCountOfRecords();

    @Query(value = "SELECT id, user_id, title, supervisor_id, submission_date," +
            "null as session_id, null as topic_id, null as answer, null as consultant_id, null as defense_score, null as department, null as faculty, " +
            "null as  has_msc_apply, null as language, null as speciality," +
            "null as topic_score,null as session_id, null as is_under_review, null as reviews_remaining" +
            " FROM Thesis where is_under_review = :condition", nativeQuery = true)
    List<Thesis> findThesesUnderReview(@Param("condition") Boolean isUnderReview);


    @Query(value = "SELECT id, user_id, title, supervisor_id, submission_date," +
            "null as session_id, null as topic_id, null as answer, null as consultant_id, null as department, null as faculty," +
            "null as  has_msc_apply, null as language, null as speciality,"+
            "null as topic_score,null as session_id, null as is_under_review, reviews_remaining "+
            " FROM Thesis where  reviews_remaining <= 0" , nativeQuery = true)
    List<Thesis> findReviewedTheses();

    @Query(value = "SELECT id, user_id, title, supervisor_id, submission_date," +
            "null as session_id, null as topic_id, null as answer, null as consultant_id, null as department, null as faculty," +
            "null as  has_msc_apply, null as language, null as speciality,"+
            "null as topic_score,null as session_id, null as is_under_review, reviews_remaining "+
            " FROM Thesis where  supervisor_id = :supervisorId" , nativeQuery = true)
    List<Thesis> findThesesBySupervisorId(@Param("supervisorId") Integer supervisorId);


}
