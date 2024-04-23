package com.example.proba.entity;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "secretaryId")
    private User secretary;
    @ManyToOne
    @JoinColumn(name = "chairmanId")
    private User chairman;

    @ManyToMany
    @JoinColumn(name = "members")
    List<User> members;

    @ManyToMany
    @JoinColumn(name = "students")
    List<User> students;


    private Date date;
    private Integer startHour;
    private Integer endHour;
    private String location;
    private String description;
    private String code;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public User getSecretary() {
        return secretary;
    }

    public void setSecretary(User secretary) {
        this.secretary = secretary;
    }

    public User getChairman() {
        return chairman;
    }

    public void setChairman(User president) {
        this.chairman = president;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }
}
