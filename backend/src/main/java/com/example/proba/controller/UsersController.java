package com.example.proba.controller;

import com.example.proba.entity.User;
import com.example.proba.service.ForgottenPasswordService;
import com.example.proba.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class UsersController {
    @Autowired
    UserService userService;

    @Autowired
    ForgottenPasswordService forgottenPasswordService;

    @PostMapping("/sendForgottenPasswordEmail")
    public void sendForgottenPasswordEmail(@RequestParam String username) {
        forgottenPasswordService.sendForgottenPasswordTokenByEmail(username);
    }

//    @PostMapping("/changePassword")
//    public void changePassword(@RequestParam String username, @RequestParam String token, @RequestParam String newPassword) {
//        forgottenPasswordService.setNewPassword(username, token, newPassword);
//    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam String username, @RequestParam String token, @RequestParam String newPassword) {
        try {
            forgottenPasswordService.setNewPassword(username, token, newPassword);
            return ResponseEntity.ok("Password changed.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or token is not valid.");
        }
    }



    @PostMapping("/changePassword_two")
    public void changePassword_two(@RequestParam Integer userId, @RequestParam String oldPassword, @RequestParam String newPassword) throws Exception {
        forgottenPasswordService.setNewPassword_two(userId, oldPassword, newPassword);
    }

    @GetMapping("/findUserById")
    public User findUserById(Integer id) {
        return userService.findUserById(id);

    }
    @GetMapping("/findAllUsers")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/findAllUsersToDisplay")
    public List<User> findAllUsersToDisplay() {return userService.findAllUsersToDisplay();}

    @PostMapping("/addUser")
    public User addUser(@RequestBody User users) {
        return userService.addUser(users);
    }


    @PutMapping("/updateUsers")
    public User updateUsers(@RequestBody User user)
    {
        return userService.updateUsers(user);
    }

    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestParam Integer id){
        userService.deleteUserById(id);
    }

    @PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesAndUser();
    }

    @PostMapping({"/registerNewUser"})
    public User registerNewUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/findUsersByRole")
    public List<User> findUsersByRole(@RequestParam Integer roleId) {
        return userService.findUsersByRole(roleId);
    }

    @GetMapping("/findUsersByRoleList")
    public List<User> findUsersByRoleList(@RequestParam List<String> roleIds) {
        return userService.findUsersByRoleList(roleIds);
    }

    @GetMapping("/findStudentsByLoggedInReviewer")
    public List<User> findStudentsByLoggedInReviewer(@RequestParam Integer userId) {
        return userService.findStudentsByLoggedInReviewer(userId);
    }

}