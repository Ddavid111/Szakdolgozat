package com.example.proba.service;

import com.example.proba.dao.ThesisDao;
import com.example.proba.dao.TopicDao;
import com.example.proba.entity.Thesis;
import com.example.proba.entity.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private ThesisDao thesisDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Topic> getTopics() {
        return (List<Topic>) topicDao.findAll();
    }

    public Topic addTopics(Topic topics)
    {
        Topic topic = topicDao.save(topics);
        Thesis actualThesis = new Thesis();
        actualThesis.setTopics(topics);
        actualThesis.setId(topic.getId());
        thesisDao.save(actualThesis);

        return topic;
    }

    public void chooseTopic(Object topicId, Object thesisId, Object topic_score) {
        String query = "UPDATE THESIS SET topic_id=" + topicId + ", topic_score=" + topic_score + " WHERE id=" + thesisId;
        jdbcTemplate.update(query);
    }

    public void initTopics() {
        if (thesisDao.getCountOfRecords() <= 0) {
            topicDao.deleteAll();
            try (BufferedReader br = new BufferedReader(new FileReader("topics/topic.txt"))) {
                String line;
                StringBuilder currentTopic = new StringBuilder();
                int index = 1;

                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        if (currentTopic.length() == 0) {
                            currentTopic.append(index).append(". ").append(line);
                        } else {
                            currentTopic.append("\n").append(line);
                        }
                    } else {
                        Topic topic = new Topic();
                        topic.setTopic(currentTopic.toString());
                        topicDao.save(topic);
                        currentTopic = new StringBuilder();
                        index++;
                    }
                }

                if (!currentTopic.toString().isEmpty()) {
                    Topic topic = new Topic();
                    topic.setTopic(currentTopic.toString());
                    topicDao.save(topic);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
