package com.example.proba.controller;

import com.example.proba.entity.Role;
import com.example.proba.entity.User;
import com.example.proba.service.ForgottenPasswordService;
import com.example.proba.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/changePassword")
    public void changePassword(@RequestParam String username, @RequestParam String token, @RequestParam String newPassword) {
        forgottenPasswordService.setNewPassword(username, token, newPassword);
    }

    @GetMapping("/findUserById")
    public List<Object> findUserById(Integer id) {
        return userService.findUserById(id);

    }
    @GetMapping("/findAllUsers")
    public List<Object> findAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User users) {
        return userService.addUser(users);
    }

    @GetMapping ("/findAllUserRoles")
    public List<Role> findAllUserRoles() {
        return userService.findAllUserRoles();
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

}