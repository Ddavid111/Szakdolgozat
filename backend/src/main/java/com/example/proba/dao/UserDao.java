package com.example.proba.dao;

import com.example.proba.entity.Role;
import com.example.proba.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {

    @Query(value = "SELECT * FROM User where role = :roleId", nativeQuery = true)
    List<User> findUsersByRole(@Param("roleId") Integer roleId);

    @Query(value = "SELECT * FROM User where role IN :roleIds", nativeQuery = true)
    List<User> findUsersByRoleList(@Param("roleIds") List<Integer> roleIds);

    @Query(value = "SELECT id, null as password, null as birth_place, null as birthday, null as pedigree_number, null as mothers_maiden_name, role," +
            " title, email, username, fullname, neptun_code, workplace, position FROM User", nativeQuery = true)
    List<User> findAllUsersToDisplay();


    @Query(value = "SELECT id, null as password, role, birth_place, birthday, pedigree_number, mothers_maiden_name, " +
            " title, email, username, fullname, neptun_code, workplace, position FROM User", nativeQuery = true)
    List<User> findAllUsersToWord();

    @Query(value = "SELECT id, null as password, null as birth_place, null as birthday, null as pedigree_number, null as mothers_maiden_name, role," +
            " title, email, username, fullname, neptun_code, workplace, position FROM User WHERE username = :username limit 1", nativeQuery = true)
    User findUserByUsername(@Param("username")String username);
}
