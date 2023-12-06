package com.example.proba.controller;

import com.example.proba.entity.Topic;
import com.example.proba.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicsController {

    @Autowired
    TopicService topicService;

    @PostMapping("/addTopics")
    public Topic addTopics(@RequestBody Topic topic){
        return topicService.addTopics(topic);


    }
}
