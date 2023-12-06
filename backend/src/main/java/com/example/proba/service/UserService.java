package com.example.proba.service;

import com.example.proba.dao.RoleDao;
import com.example.proba.dao.UserDao;
import com.example.proba.entity.Role;
import com.example.proba.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Object> findAllUsers(){
        String query = "select * from user";
        var resultSet = jdbcTemplate.queryForList(query);
        List<Object> result = new ArrayList<>();
        resultSet.forEach((key) -> {
            result.add(key.values());
        });

        return result;
    }

    public User findUserByName(String username) {
        List<User> users = (List<User>) userDao.findAll();
        for (User user : users) {
            if(user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public List<Object> findUserById (Integer id) {
        String query = "select * from user where user_id="+id;
        var resultSet = jdbcTemplate.queryForList(query);
        List<Object> result = new ArrayList<>();
        resultSet.forEach((key) -> {
            result.add(key.values());
        });

        return result;
    }


    public User addUser(User users)
    {
        users.setPassword(getEncodedPassword(users.getPassword()));
        User user = userDao.save(users);

        String query = "INSERT INTO ROLE VALUES (" + user.getUserId() + ", " + user.getRoleId() + ")";
        jdbcTemplate.update(query);


//        userRolesDao.save(actualUserRole);

        return user;

    }

    public void initRolesAndUser() {

        User user = new User();
        user.setEmail("email");
        user.setPassword(getEncodedPassword("password"));
        user.setTitle("Dr.");
        user.setName("name");
        user.setEmail("zalman2020201@gmail.com");
        user.setMothersMaidenName("anyja neve");
        user.setBirthPlace("Moscow");
        user.setWorkplace("Kreml");
        user.setRoleId(1); // elnok
        user.setNeptunCode("ABC123");
        user.setBirthday(new Date());
        System.out.println("Users before saving: " + user);
       User savedUser =  userDao.save(user);
       System.out.println(savedUser.toString());
        Role actualUserRole = new Role();
        actualUserRole.setUser(savedUser);
        actualUserRole.setRoles(Role.Roles.values()[savedUser.getRoleId()]);
        System.out.println("actualUserRole " + actualUserRole.toString());
        actualUserRole.setUserId(savedUser.getUserId());

        String query = "INSERT INTO ROLE VALUES (" + savedUser.getUserId() + ", " + savedUser.getRoleId() + ")";
        jdbcTemplate.update(query);


    }

    public List<Role> findAllUserRoles(){

      return (List<Role>) roleDao.findAll();
    }

    public void deleteUserById(Integer id){

        String query = "delete from role where user_id = " + id;
        jdbcTemplate.update(query);
        query = "delete from user where user_id = " + id;
        jdbcTemplate.update(query);

    }

    public void changePassword(User user, String newPassword) {
        String query = "UPDATE USER SET PASSWORD = '" + passwordEncoder.encode(newPassword) + "' WHERE user_id = " + user.getUserId();
        jdbcTemplate.update(query);

        System.out.println("Pw changed for user " + user.getName());
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}