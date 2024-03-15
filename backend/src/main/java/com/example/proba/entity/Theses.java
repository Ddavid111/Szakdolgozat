package com.example.proba.entity;

import javax.persistence.*;

import java.util.Set;
import java.util.Date;

@Entity

public class Theses {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(mappedBy = "reviewId", cascade = CascadeType.ALL)
   // @JoinTable("Review")
    Set<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "sessionId", insertable = false, updatable = false)
    Session session;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "supervisorId", insertable = false, updatable = false)
    User supervisor;

    @ManyToOne
    @JoinColumn(name = "consultantId", insertable = false, updatable = false)
    User consultant;

    @ManyToOne
    @JoinColumn(name = "topicsId", insertable = false, updatable = false)
    Topic topic;

    private Integer userId;
    private Integer supervisorId;
    private Integer consultantId;
    private String title;
    private String faculty;
    private String department;
    private String speciality;
    private String language;
    private Boolean hasMscApply;
    private Integer thesisPdfId;
    private Integer supplementId;
    private Date submissionDate;
    private String answer;
    private Integer topicScore;
    private Integer defenseScore;
    private Integer subjectScore;
    private Integer finalScore;
    private Boolean isUnderReview = false;
    private Integer reviewsRemaining = 2; // azt jelenti, h van-e rá bírálat vagy még nem

    public void decreaseRemainingReviews(){
        this.reviewsRemaining --;

    }

    public Integer getReviewsRemaining() {
        return reviewsRemaining;
    }

    public Boolean getUnderReview() {
        return isUnderReview;
    }

    public void setUnderReview(Boolean underReview) {
        isUnderReview = underReview;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer studentId) {
        this.userId = studentId;
    }

    public Integer getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Integer supervisorId) {
        this.supervisorId = supervisorId;
    }

    public Integer getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(Integer consultantId) {
        this.consultantId = consultantId;
    }

    public User getConsultant() {
        return consultant;
    }

    public void setConsultant(User consultant) {
        this.consultant = consultant;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getHasMscApply() {
        return hasMscApply;
    }

    public void setHasMscApply(Boolean hasMscApply) {
        this.hasMscApply = hasMscApply;
    }

    public Integer getThesisPdfId() {
        return thesisPdfId;
    }

    public void setThesisPdfId(Integer thesisPdfId) {
        this.thesisPdfId = thesisPdfId;
    }

    public Integer getSupplementId() {
        return supplementId;
    }

    public void setSupplementId(Integer supplementId) {
        this.supplementId = supplementId;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Topic getTopics() {
        return topic;
    }

    public void setTopics(Topic topic) {
        this.topic = topic;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getTopicScore() {
        return topicScore;
    }

    public void setTopicScore(Integer topicScore) {
        this.topicScore = topicScore;
    }

    public Integer getDefenseScore() {
        return defenseScore;
    }

    public void setDefenseScore(Integer defenseScore) {
        this.defenseScore = defenseScore;
    }

    public Integer getSubjectScore() {
        return subjectScore;
    }

    public void setSubjectScore(Integer subjectScore) {
        this.subjectScore = subjectScore;
    }

    public Integer getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

//    public Set<Review> getTheseses() {
//        return reviews;
//    }
//
//    public void setTheseses(Set<Review> theseses) {
//        this.reviews = theseses;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }

    public Session getSessions() {
        return session;
    }

    public void setSessions(Session session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "Theses{" +
                "id=" + id +
                ", reviews=" + reviews +
                ", session=" + session +
                ", user=" + user +
                ", supervisor=" + supervisor +
                ", consultant=" + consultant +
                ", topic=" + topic +
                ", userId=" + userId +
                ", supervisorId=" + supervisorId +
                ", consultantId=" + consultantId +
                ", title='" + title + '\'' +
                ", faculty='" + faculty + '\'' +
                ", department='" + department + '\'' +
                ", speciality='" + speciality + '\'' +
                ", language='" + language + '\'' +
                ", hasMscApply=" + hasMscApply +
                ", thesisPdfId=" + thesisPdfId +
                ", supplementId=" + supplementId +
                ", submissionDate=" + submissionDate +
                ", answer='" + answer + '\'' +
                ", topicScore=" + topicScore +
                ", defenseScore=" + defenseScore +
                ", subjectScore=" + subjectScore +
                ", finalScore=" + finalScore +
                ", isUnderReview=" + isUnderReview +
                ", reviewsRemaining=" + reviewsRemaining +
                '}';
    }
}
