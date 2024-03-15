package com.example.proba.controller;

import com.example.proba.entity.Session;
import com.example.proba.entity.User;
import com.example.proba.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/addSession")
    public Session addSession(@RequestBody Session session){

        return sessionService.addSessions(session);


    }

    @GetMapping("/getSessionList")
    public List<Session> getSessionList() {
        return sessionService.getSessionList();

    }

    @GetMapping("/getSessionsListToDisplay")
    public List<Session> getSessionsListToDisplay(){
        return sessionService.getSessionsListToDisplay();
    }



}
