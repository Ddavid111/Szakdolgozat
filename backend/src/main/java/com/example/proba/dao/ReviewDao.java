package com.example.proba.dao;

import com.example.proba.entity.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewDao extends CrudRepository<Review, Integer> {

}
