package com.example.proba.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;
    @ManyToOne
    @JsonIgnore // without this the app crashes
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    User user;
    @Enumerated(EnumType.ORDINAL)
    private Roles roles;

    public enum Roles {
        Hallgato,
        Elnok,
        Jegyzo,
        Biralo,
        Temavezeto

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserRoles{" +
                "userId=" + userId +
                //         ", users=" + users +
                ", roles=" + roles +
                '}';
    }
}
