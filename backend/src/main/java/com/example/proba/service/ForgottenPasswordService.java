package com.example.proba.service;

import com.example.proba.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ForgottenPasswordService {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    UserService userService;

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

        public boolean isValidToken(String username, String token) {
            for(ForgottenPasswordToken element : forgottenPasswordTokens) {
                if(element.username.equals(username) && element.token.equals(token)
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
            if(isValidToken(username, token)) {
                userService.changePassword(userService.findUserByName(username), newPassword);
            }
            else  {
                System.err.println("Username or token is not valid.");
            }
        }

    public void sendForgottenPasswordTokenByEmail(String username) {
        User user = userService.findUserByName(username);

        if(user == null) {
            System.err.println("Username not found.");
            throw new UsernameNotFoundException("Username not found.");
        }

        String email = user.getEmail();
        String emailSubject = "Restore your password";
        String emailBody = "You have requested a token for restoring your password.\n";

        ForgottenPasswordToken forgottenPasswordToken = new ForgottenPasswordToken(username);
        String randomToken = forgottenPasswordToken.token;

        forgottenPasswordTokens.add(forgottenPasswordToken);

        if (randomToken != null) {
            emailBody += "Your token is the following: ";
            emailBody += randomToken;
            emailBody += "\nYou can use the token for 10 minutes.";

        }
        else {
            emailBody += "There was an error while generating a token for your password. Please contact the administrator.";

        }

        emailSenderService.sendEmail(email, emailSubject, emailBody);
    }
}
