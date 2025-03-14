package com.hellfire.myBlogger.controller;


import com.hellfire.myBlogger.config.JwtProvider;
import com.hellfire.myBlogger.models.User;
import com.hellfire.myBlogger.response.ResponseStatus;
import com.hellfire.myBlogger.response.UserDTO;
import com.hellfire.myBlogger.response.UserDetailsResponse;
import com.hellfire.myBlogger.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping()
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/getUser/{id}")
    public UserDTO getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }
    @DeleteMapping("/deleteUser/{id}")
    public void deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/getUserDetails")
    public UserDetailsResponse getUserDetails(@RequestHeader("Authorization") String token) {
            UserDetailsResponse userDetails = new UserDetailsResponse();

            String email= JwtProvider.getEmailFromJwtToken(token);
        try{
            User user=userService.getUserByEmail(email);
            userDetails.setEmail(user.getEmail());
            userDetails.setUserName(user.getUsername());
            userDetails.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            userDetails.setStatus(ResponseStatus.FAILURE);
            throw new RuntimeException(e);
        }
        return userDetails;
    }



}
