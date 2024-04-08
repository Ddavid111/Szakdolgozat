package com.example.proba.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reviewerId")
    User user;

    @ManyToOne
    @JoinColumn(name = "thesisId")
    Thesis thesis;

    private Date invitationDate;
    private Integer Score;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String city;
    private Date submissionDate;

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + id +
                ", invitationDate=" + invitationDate +
                ", Score=" + Score +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", submissionDate=" + submissionDate +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer reviewId) {
        this.id = reviewId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Thesis getTheseses() {
        return thesis;
    }

    public void setTheseses(Thesis thesis) {
        this.thesis = thesis;
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

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }
}
