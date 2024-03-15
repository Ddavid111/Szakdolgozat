package com.example.proba.controller;

import com.example.proba.entity.Topic;
import com.example.proba.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class TopicsController {

    @Autowired
    TopicService topicService;

    @PostConstruct
    public void initTopics() {
        topicService.initTopics();
    }

    @GetMapping("/getTopics")
    public List<Topic> getTopics() {
        return topicService.getTopics();
    }

    @PostMapping("/addTopics")
    public Topic addTopics(@RequestBody Topic topic){
        return topicService.addTopics(topic);
    }

    @PostMapping("/chooseTopic")
    public void chooseTopic(@RequestParam Object topicId, @RequestParam Object thesisId, @RequestParam Object topic_score) {
        topicService.chooseTopic(topicId, thesisId, topic_score);
    }
}
