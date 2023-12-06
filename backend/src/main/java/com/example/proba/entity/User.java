package com.example.proba.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<Role> roles;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    Set<Review> reviews;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    Set<Theses> theseses;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "supervisor", cascade = CascadeType.ALL)
    Set<Theses> thesesess;

    /*@OneToMany(mappedBy = "reviews")
    Set<Reviews> reviews;*/
    
    private String email;
    private String neptunCode;
    private String password;
    private String title;
    private String name;
    private Date birthday;
    private Integer roleId;
    private String birthPlace;
    private String mothersMaidenName;
    private String workplace;


    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
             //   ", roles=" + roles +
                ", email='" + email + '\'' +
                ", neptunCode='" + neptunCode + '\'' +
                ", password='" + password + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", roleId=" + roleId +
                ", birthPlace='" + birthPlace + '\'' +
                ", mothersMaidenName='" + mothersMaidenName + '\'' +
                ", workplace='" + workplace + '\'' +
                '}';
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Role.Roles roles) {
        Set<Role> roleHashSet = new HashSet<>();
      //  roleHashSet.add();
        this.roles = roleHashSet;
    }
}
