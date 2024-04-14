package com.example.proba.service;

import com.example.proba.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class ForgottenPasswordService {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    UserService userService;


    @Autowired
    private AuthenticationManager authenticationManager;

    private List<ForgottenPasswordToken> forgottenPasswordTokens = new ArrayList<>();

    public static class ForgottenPasswordToken {
        private String username;

        private String token;
        private Date created;
        private Date expiration;

        public ForgottenPasswordToken(String username) {
            Calendar now = Calendar.getInstance();
            this.username = username;
            this.token = UUID.randomUUID().toString();
            this.created = new Date(now.getTimeInMillis());
            this.expiration = new Date(now.getTimeInMillis() + (1000 * 60 * 10)); // add 10 minutes

        }
    }


    public boolean isValidPassword(Integer userId, String password) {
        String encryptedPassword = userService.getEncodedPassword(password);
        User user = userService.findUserById(userId);
        String temp = userService.getEncodedPassword("asd");
        System.out.println(temp);
        //System.out.println(user.getPassword()+"\n"+encryptedPassword );
        // $2a$10$1i3YDdcMXq.eTmaV8QyViuHyJeleCdVZdzkMCgsEZperSeckieoSC

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), password));
            return true;
        } catch (Exception e) {
            System.err.println("Wrong pw.");
            return false;
        }
//
//            if(encryptedPassword.equals(user.getPassword())){
//
//                return true;
//            }
//            return false;
//        }
    }

    public boolean isValidToken(String username, String token) {
        for (ForgottenPasswordToken element : forgottenPasswordTokens) {
            if (element.username.equals(username) && element.token.equals(token)
                    && element.expiration.after(new Date())) {
                return true;
            }
        }
        return false;
    }


//        public String setForgottenPasswordToken(String username) {
//            forgottenPasswordTokens.add(new ForgottenPasswordToken(username));
//            return forgottenPasswordTokens.get(forgottenPasswordTokens.size()-1).token;
//        }

    // If there will be bugs because of the previous function, we can use the following logic to retrieve the token:

//        public String getValidTokenByUsername(String username) {
//
//            for (ForgottenPasswordToken element : forgottenPasswordTokens) {
//                if (element.username.equals(username) && element.token.equals(token)
//                        && element.expiration.before(new Date())) {
//                    return token;
//                }
//            }
//
//            return null;
//        }

    public void setNewPassword(String username, String token, String newPassword) {
        if (isValidToken(username, token)) {
            userService.changePassword(userService.findUserByName(username), newPassword);
            System.out.println("Password changed.");
        } else {
            System.err.println("Username or token is not valid.");
        }
    }

    public void setNewPassword_two(Integer userId, String oldPassword, String newPassword) { //throws Exception {
        User user = userService.findUserById(userId);
        if (isValidPassword(user.getId(), oldPassword)) {
            userService.changePassword_two(user, newPassword);
        }

//       } else {
//           throw new Exception("No such user for that userID or bad pw.");
//       }

    }

    public void sendForgottenPasswordTokenByEmail(String username) {
        User user = userService.findUserByName(username);

        if (user == null) {
            System.err.println("Username not found.");
            throw new UsernameNotFoundException("Username not found.");
        }

        try {

            BufferedReader reader = new BufferedReader(new FileReader("email/forgotten_password.txt"));
            StringBuilder emailBodyBuilder = new StringBuilder();
            String line;
            ForgottenPasswordToken forgottenPasswordToken = new ForgottenPasswordToken(username);
            String randomToken = forgottenPasswordToken.token;
            while ((line = reader.readLine()) != null) {

                if (line.contains("[Név]")) {
                    line = line.replace("[Név]", username);
                }
                if (randomToken != null) {
                    if (line.contains("[token]")) {
                        line = line.replace("[token]", randomToken);
                    }
                }
                else {
                    line += "There was an error while generating a token for your password. Please contact the administrator.";
                }

                emailBodyBuilder.append(line).append("\n");
            }
            reader.close();
            String emailBody = emailBodyBuilder.toString();

            String email = user.getEmail();
            String emailSubject = "Elfelejtett jelszó";
            emailSenderService.sendEmail(email, emailSubject, emailBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}