package com.example.proba.entity;

import javax.persistence.*;

import java.util.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    Set<Review> review;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    Set<Thesis> theses;


    @Enumerated(EnumType.ORDINAL)
    private Role role;
    private String email;
    private String neptunCode;
    private String password;
    private String title;
    private String username;
    private String fullname;
    private Date birthday;
    private String birthPlace;
    private String mothersMaidenName;
    private String workplace;
    private String pedigreeNumber;
    private String position;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + id +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", neptunCode='" + neptunCode + '\'' +
                ", password='" + password + '\'' +
                ", title='" + title + '\'' +
                ", username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", birthday=" + birthday +
                ", birthPlace='" + birthPlace + '\'' +
                ", mothersMaidenName='" + mothersMaidenName + '\'' +
                ", workplace='" + workplace + '\'' +
                ", pedigreeNumber='" + pedigreeNumber + '\'' +
                ", post='" + position + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer userId) {
        this.id = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNeptunCode() {
        return neptunCode;
    }

    public void setNeptunCode(String neptunCode) {
        this.neptunCode = neptunCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getMothersMaidenName() {
        return mothersMaidenName;
    }

    public void setMothersMaidenName(String mothersMaidenName) {
        this.mothersMaidenName = mothersMaidenName;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getPedigreeNumber() {
        return pedigreeNumber;
    }

    public void setPedigreeNumber(String pedigreeNumber) {
        this.pedigreeNumber = pedigreeNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String post) {
        this.position = post;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }




}
