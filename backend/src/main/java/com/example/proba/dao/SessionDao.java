package com.example.proba.dao;

import com.example.proba.entity.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface SessionDao extends CrudRepository<Session, Integer> {

    
    @Query (value = "SELECT code, date, id, description, end_hour, location, notary, president, start_hour FROM SESSION", nativeQuery = true)
    List<Session> getSessionsListToDisplay();


}
