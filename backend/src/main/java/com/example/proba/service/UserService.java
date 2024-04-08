package com.example.proba.service;

import com.example.proba.dao.ReviewDao;
import com.example.proba.dao.ThesisDao;
import com.example.proba.dao.UserDao;
import com.example.proba.entity.Review;
import com.example.proba.entity.Role;
import com.example.proba.entity.Thesis;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private ThesisDao thesisDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public User addUser(User users)
    {
        users.setPassword(getEncodedPassword(users.getPassword()));

        return userDao.save(users);
    }

    public User updateUsers(User user)
    {
        Integer id = user.getId();
        User temp = userDao.findById(id).get();

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
        temp.setRole(user.getRole());
        if(user.getRole() == Role.Hallgató) {
            temp.setPosition(null);
        }
        else{
            temp.setPosition(user.getPosition());
        }

        return userDao.save(temp);
    }

    public void deleteUserById(Integer id){
        String userQuery = "delete from user where id = " + id;

        jdbcTemplate.update(userQuery);
    }

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

    public List<User> findUsersByRoleList(List<String> roleIds) {
        List<Integer> roleIdsInt = new ArrayList<>();
        for (String roleId : roleIds) {
            roleIdsInt.add(Role.valueOf(roleId).ordinal());
        }

        Iterable<User> userIterable = userDao.findUsersByRoleList(roleIdsInt);
        List<User> userList = new ArrayList<>();

        userIterable.forEach(userList::add);
        return userList;
    }

    public User findUserByName(String username) {
        return userDao.findUserByUsername(username);
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

    public List<User> findStudentsByLoggedInReviewer (Integer userId)
    {
        User user = findUserById(userId);
        Role role = user.getRole();
        switch(role){
            case Bíráló:{
                List<Review> reviews = reviewDao.findReviewsByReviewerId(userId);
                List<User> students = new ArrayList<>();
                for(Review review: reviews)
                {
                    if(review.getScore() == null) {
                        User student = review.getTheseses().getUser();
                        if (!students.contains(student)) {
                            students.add(student);
                        }
                    }
                }
                return students;
            }
            case Témavezető:{
                List<Thesis> theses = thesisDao.findThesesBySupervisorId(userId);
                List<User> students = new ArrayList<>();
                for(Thesis thesis: theses)
                {
                    students.add(findUserById(thesis.getUser().getId()));
                }
                return students;
            }
            default:{
                return null;
            }
        }

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
        user.setNeptunCode("ABC123");
        user.setBirthday(new Date());

        user.setRole(Role.Hallgató);

        userDao.save(user);


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
        user.setPosition("egyetemi adjunktus");
        user.setNeptunCode("BCD123");
        user.setBirthday(new Date());

        user.setRole(Role.Témavezető);

        userDao.save(user);

        // ADMIN felvitele

        user = new User();
        user.setPassword(getEncodedPassword("admin"));
        user.setTitle("Dr.");
        user.setUsername("admin");
        user.setFullname("Minta Admin");
        user.setEmail("zalman2020201@gmail.com");
        user.setMothersMaidenName("admin anyja neve");
        user.setBirthPlace("Lyukóbánya");
        user.setWorkplace("Cigánysor");
        user.setPedigreeNumber("T34458");
        user.setPosition("egyetemi tanár");
        user.setNeptunCode("ABCDEF");
        user.setBirthday(new Date());

        user.setRole(Role.ADMIN);

        userDao.save(user);

    }

    public void changePassword(User user, String newPassword) {
        String query = "UPDATE USER SET PASSWORD = '" + passwordEncoder.encode(newPassword) + "' WHERE id = " + user.getId();
        jdbcTemplate.update(query);

        System.out.println("Pw changed for user " + user.getUsername());
    }

    public void changePassword_two(User user, String newPassword) {
        String query = "UPDATE USER SET PASSWORD = '" + passwordEncoder.encode(newPassword) + "' WHERE id = " + user.getId();
        jdbcTemplate.update(query);

        System.out.println("Pw changed for user " + user.getPassword());
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}