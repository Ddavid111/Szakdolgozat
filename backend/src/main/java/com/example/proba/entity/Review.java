package com.example.proba.entity;


import javax.persistence.*;


import java.util.Date;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    //private Integer reviewerId;
    /*@ManyToOne
    @JoinColumn(name = "reviewerId",insertable = false,updatable = false)
    Reviews reviews;*/

    @ManyToOne
    @JoinColumn(name= "reviewerId")
    User user;

    @ManyToOne
    @JoinColumn(name = "thesisId")
    Theses theses;

    //private Integer thesisId;
    private Date invitationDate;
    private Integer Score;
    private String description;
    private String city;
    private Date invitationAcceptionDate;
    private Date responseDate;
    private Date submissionDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    /*public Integer getThesisId() {
        return thesisId;
    }

    public void setThesisId(Integer thesisId) {
        this.thesisId = thesisId;
    }*/

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
