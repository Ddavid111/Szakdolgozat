package com.example.proba.dao;

import com.example.proba.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {

    @Query(value = "SELECT * FROM User where role_id = :roleId", nativeQuery = true)
    List<User> findUsersByRole(@Param("roleId") Integer roleId);

    @Query(value = "SELECT * FROM User where role_id IN :roleIds", nativeQuery = true)
    List<User> findUsersByRoleList(@Param("roleIds") List<Integer> roleIds);

    @Query(value = "SELECT user_id, null as password, null as birth_place, null as birthday, null as pedigree_number, null as mothers_maiden_name, " +
            " title, email, username, fullname, neptun_code, workplace, post, role_id FROM User", nativeQuery = true)
    List<User> findAllUsersToDisplay();


    @Query(value = "SELECT user_id, null as password, birth_place, birthday, pedigree_number, mothers_maiden_name, " +
            " title, email, username, fullname, neptun_code, workplace, post, role_id FROM User", nativeQuery = true)
    List<User> findAllUsersToWord();
}
