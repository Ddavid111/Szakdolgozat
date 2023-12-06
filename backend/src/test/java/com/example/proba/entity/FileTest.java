package com.example.proba.entity;


import org.junit.jupiter.api.Test;


import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
class FileTest {


    @Test
    void getId() {
        File file = new File();
        file.setId(5);
        assertEquals(5,file.getId());
    }

    @Test
    void setId() {
        File file = new File();
        file.setId(5);
        assertEquals(5,file.getId());
    }

    @Test
    void getName() {
        File file = new File();
        file.setName("asd");
        assertEquals("asd",file.getName());
    }

    @Test
    void setName() {
        File file = new File();
        file.setName("asd");
        assertEquals("asd",file.getName());
    }

    @Test
    void getUploadTime() {
        File file = new File();

        file.setUploadTime(new Date(2023,Calendar.AUGUST,22));
        assertEquals(new Date(2023, Calendar.AUGUST,22), file.getUploadTime());

    }

    @Test
    void setUploadTime() {
        File file = new File();

        file.setUploadTime(new Date(2023,Calendar.AUGUST,22));
        assertEquals(new Date(2023,Calendar.AUGUST,22), file.getUploadTime());
    }
}