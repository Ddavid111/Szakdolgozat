package com.example.proba.controller;

import com.example.proba.entity.User;
import com.example.proba.service.ForgottenPasswordService;
import com.example.proba.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> changePassword(@RequestParam String username, @RequestParam String token, @RequestParam String newPassword) {
        try {
            forgottenPasswordService.setNewPassword(username, token, newPassword);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password changed successfully.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Username or token is not valid.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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