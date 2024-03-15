package com.example.proba.dao;

import com.example.proba.entity.File;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FileDao extends CrudRepository<File, Integer> {

    // PK mindig kell, nem lehet NULL a query-ben, különben JPA exception
   @Query(value = "select id, name as name, NULL as upload_time, uuid as uuid, thesis_id  from file", nativeQuery = true)
    List<File> findByTheses();

}
