package com.example.proba.service;


import com.example.proba.dao.ThesesDao;
import com.example.proba.dao.TopicDao;
import com.example.proba.entity.Theses;
import com.example.proba.entity.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private ThesesDao thesesDao;

    public Topic addTopics(Topic topics)
    {
        Topic topic = topicDao.save(topics);
        Theses actualTheses = new Theses();
        actualTheses.setTopics(topics);
        actualTheses.setId(topic.getId());
        thesesDao.save(actualTheses);

        return topic;
    }

}
