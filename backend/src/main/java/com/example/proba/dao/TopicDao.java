package com.example.proba.dao;

import com.example.proba.entity.Topic;
import org.springframework.data.repository.CrudRepository;

public interface TopicDao extends CrudRepository<Topic, Integer> {
}
