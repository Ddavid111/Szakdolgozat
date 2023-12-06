package com.example.proba.service;


import com.example.proba.dao.UserDao;
import com.example.proba.entity.JwtRequest;
import com.example.proba.entity.JwtResponse;
import com.example.proba.entity.User;
import com.example.proba.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDao usersDao;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // FIND BY NAME!!
        List<User> usersList = (List<User>) usersDao.findAll();
        User user = null;
     //   Users user = usersDao.findById(1).get();
        for(User u: usersList) {
            if(u.getName().equals(username)) {
                user = u;
            }
        }

         // we need to find the user's userID by the username we got from the parameter

        if(user != null) {
            return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), getAuthorities(user));
        } else {
            throw new UsernameNotFoundException("Username is not valid!");
        }
    }

    private Set getAuthorities(User user) {
        Set authorities = new HashSet();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoles().toString().toUpperCase()));
        });
        return authorities;
    }

    // 1st
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String username = jwtRequest.getUserName();
        String password = jwtRequest.getUserPassword();

        authenticate(username, password);

        final UserDetails userDetails = loadUserByUsername(username);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        List<User> usersList = (List<User>) usersDao.findAll();
        User user = null;
        for(User u: usersList) {
            if(u.getName().equals(username)){
                user = u;
            }
        }

       // Users user = usersDao.findById(1).get(); // we need to find the user's userID by the username we got from line 54

        return new JwtResponse(user, newGeneratedToken);

    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException disabledException) {
            throw new Exception("User is disabled!");
        } catch (BadCredentialsException badCredentialsException) {
            throw new Exception("Bad credentials from user!");
        }

    }
}