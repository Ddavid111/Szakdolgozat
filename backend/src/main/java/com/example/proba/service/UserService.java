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

    public List<User> findAllUsers(){
        Iterable<User> optionalUsers = userDao.findAll();
        List<User> userList = new ArrayList<>();

        optionalUsers.forEach(userList::add);
        return userList;
    }
    public List<User> findAllUsersToDisplay(){
        Iterable<User> optionalUsers = userDao.findAllUsersToDisplay();
        List<User> userList = new ArrayList<>();

        optionalUsers.forEach(userList::add);
        return userList;
    }



    public List<User> findUsersByRole(Integer roleId) {
        Iterable<User> userIterable = userDao.findUsersByRole(roleId);
        List<User> userList = new ArrayList<>();

        userIterable.forEach(userList::add);
        return userList;
    }

    public List<User> findUsersByRoleList(List<Integer> roleIds) {
        Iterable<User> userIterable = userDao.findUsersByRoleList(roleIds);
        List<User> userList = new ArrayList<>();

        userIterable.forEach(userList::add);
        return userList;
    }

    public User findUserByName(String username) {
        List<User> users = (List<User>) userDao.findAll();
        for (User user : users) {
            if(user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public User findUserById (Integer id) {
        Optional<User> users = userDao.findById(id);
        if(users.isPresent()) {
            return users.get();
        } else {
            try {
                throw new Exception("No user with this ID.");
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
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

        // HALLGATO felvitele
        User user = new User();
        user.setPassword(getEncodedPassword("password"));
        user.setTitle("Dr.");
        user.setUsername("username");
        user.setFullname("Minta Felhasználó");
        user.setEmail("zalman2020201@gmail.com");
        user.setMothersMaidenName("anyja neve");
        user.setBirthPlace("Moscow");
        user.setWorkplace("Kreml");
        user.setPedigreeNumber("T46898");
        //user.setPost(); // hallgatónak nincs beosztása
        user.setRoleId(0); // hallgato
        user.setNeptunCode("ABC123");
        user.setBirthday(new Date());

        User savedUser =  userDao.save(user);

        Role actualUserRole = new Role();
        actualUserRole.setUser(savedUser);
        actualUserRole.setRoles(Role.Roles.values()[savedUser.getRoleId()]);
        actualUserRole.setUserId(savedUser.getUserId());

        String query = "INSERT INTO ROLE VALUES (" + savedUser.getUserId() + ", " + savedUser.getRoleId() + ")";
        jdbcTemplate.update(query);

        // TEMAVEZETO felvitele

        user = new User();
        user.setPassword(getEncodedPassword("root"));
        user.setTitle("Dr.");
        user.setUsername("root");
        user.setFullname("Minta Témavezető");
        user.setEmail("zalman2020201@gmail.com");
        user.setMothersMaidenName("témavezető anyja neve");
        user.setBirthPlace("Bukarest");
        user.setWorkplace("Parlament");
        user.setPedigreeNumber("T34434");
        user.setPost("egyetemi adjunktus");
        user.setRoleId(4); // témavezető
        user.setNeptunCode("BCD123");
        user.setBirthday(new Date());

        savedUser = userDao.save(user);

        actualUserRole = new Role();
        actualUserRole.setUser(savedUser);
        actualUserRole.setRoles(Role.Roles.values()[savedUser.getRoleId()]);
        actualUserRole.setUserId(savedUser.getUserId());

        query = "INSERT INTO ROLE VALUES (" + savedUser.getUserId() + ", " + savedUser.getRoleId() + ")";
        jdbcTemplate.update(query);


    }

    public List<Role> findAllUserRoles(){

      return (List<Role>) roleDao.findAll();
    }

    public User updateUsers(User user)
    {
        Integer id = user.getUserId();
        User temp = userDao.findById(id).get();
        Role tempp = roleDao.findById(id).get();

        temp.setTitle(user.getTitle());
        temp.setBirthday(user.getBirthday());
        temp.setEmail(user.getEmail());
        temp.setUsername(user.getUsername());
        temp.setFullname(user.getFullname());
        temp.setNeptunCode(user.getNeptunCode());
        temp.setMothersMaidenName(user.getMothersMaidenName());
        temp.setBirthPlace(user.getBirthPlace());
        temp.setWorkplace(user.getWorkplace());
        temp.setPedigreeNumber(user.getPedigreeNumber());
        if(user.getRoleId() == 0) {
            temp.setPost(null);
        }
        else{
            temp.setPost(user.getPost());
        }
        tempp.setRoles(tempp.getRoles().values()[user.getRoleId()]);
        temp.setRoleId(user.getRoleId());

        roleDao.save(tempp);
        return userDao.save(temp);
    }

    public void deleteUserById(Integer id){
        String roleQuery = "delete from role where user_id = " + id;
        String userQuery = "delete from user where user_id = " + id;

        jdbcTemplate.update(roleQuery);
        jdbcTemplate.update(userQuery);
    }

    public void changePassword(User user, String newPassword) {
        String query = "UPDATE USER SET PASSWORD = '" + passwordEncoder.encode(newPassword) + "' WHERE user_id = " + user.getUserId();
        jdbcTemplate.update(query);

        System.out.println("Pw changed for user " + user.getUsername());
    }

    public void changePassword_two(User user, String newPassword) {
        String query = "UPDATE USER SET PASSWORD = '" + passwordEncoder.encode(newPassword) + "' WHERE user_id = " + user.getUserId();
        jdbcTemplate.update(query);

        System.out.println("Pw changed for user " + user.getPassword());
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}