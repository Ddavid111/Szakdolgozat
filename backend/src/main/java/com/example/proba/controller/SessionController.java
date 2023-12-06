package com.example.proba.controller;

import com.example.proba.entity.Session;
import com.example.proba.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/addSession")
    public Session addSession(@RequestBody Session session){

        return sessionService.addSessions(session);


    }



}
