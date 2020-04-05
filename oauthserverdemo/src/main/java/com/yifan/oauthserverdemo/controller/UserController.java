package com.yifan.oauthserverdemo.controller;

import com.yifan.oauthserverdemo.model.UserProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @RequestMapping("/api/users/me")
    public ResponseEntity<UserProfile> profile()
    {
        //Build some dummy data to return for testing
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = user.getUsername();
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        String email = username + "@outlook.com";

        UserProfile profile = new UserProfile();
        profile.setName(username);
        profile.setEmail(email);

        return ResponseEntity.ok(profile);
    }

    @GetMapping("/api/hello")
    public  ResponseEntity<String> hello(){
        return ResponseEntity.ok("Just Hello");
    }
}
