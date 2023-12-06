package com.example.proba.service;

import com.example.proba.dao.SessionDao;
import com.example.proba.entity.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SessionService {

    @Autowired
    private SessionDao sessionDao;


    public Session addSessions(Session session)
    {

        return sessionDao.save(session);

    }

}
