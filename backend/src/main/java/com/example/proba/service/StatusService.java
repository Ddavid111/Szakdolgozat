package com.example.proba.service;

import com.example.proba.dao.FileDao;
import com.example.proba.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.example.proba.util.Triple;

@Service
public class StatusService {

    @Autowired
    FileDao fileDao;

    public List<Triple<Integer, String, String>> getNeccessaryData() {
        List<Triple<Integer, String, String>> fileData = new ArrayList<>();

        // lejön a query
        List<File> tempData = fileDao.findByTheses();

        for (File f : tempData) {
            System.out.println(f.getThesis().getId() + "\t" + f.getName() + "\t" + f.getUuid());
            fileData.add(new Triple<>(f.getThesis().getId(), f.getName(), f.getUuid()));
        }

        return fileData;
    }


}


