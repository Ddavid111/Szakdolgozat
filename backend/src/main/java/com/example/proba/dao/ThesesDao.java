package com.example.proba.dao;

import com.example.proba.entity.Theses;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThesesDao extends CrudRepository<Theses, Integer> {
}
