package com.example.proba.entity;

import javax.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private Date uploadTime;

    private String uuid;


    @ManyToOne
    @JoinColumn(name = "thesis_id")
    private Theses thesis;

    public Theses getThesis() {
        return thesis;
    }

    public void setThesis(Theses thesis) {
        this.thesis = thesis;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}
