package com.example.proba.entity;


import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "reviewerId")
    User user;

    @ManyToOne
    @JoinColumn(name = "thesisId")
    Theses theses;

    private Date invitationDate;
    private Integer Score;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String city;
    private Date invitationAcceptionDate;
    private Date responseDate;
    private Date submissionDate;

    @Override
    public String toString() {
        return "Review{" +
                "id=" + reviewId +
                ", user=" + user.toString() +
                ", theses=" + theses +
                ", invitationDate=" + invitationDate +
                ", Score=" + Score +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", invitationAcceptionDate=" + invitationAcceptionDate +
                ", responseDate=" + responseDate +
                ", submissionDate=" + submissionDate +
                '}';
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Theses getTheseses() {
        return theses;
    }

    public void setTheseses(Theses theses) {
        this.theses = theses;
    }

    public Date getInvitationDate() {
        return invitationDate;
    }

    public void setInvitationDate(Date invitationDate) {
        this.invitationDate = invitationDate;
    }

    public Integer getScore() {
        return Score;
    }

    public void setScore(Integer score) {
        Score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getInvitationAcceptionDate() {
        return invitationAcceptionDate;
    }

    public void setInvitationAcceptionDate(Date invitationAcceptionDate) {
        this.invitationAcceptionDate = invitationAcceptionDate;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }
}
