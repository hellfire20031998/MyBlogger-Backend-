package com.hellfire.myBlogger.controller;


import com.hellfire.myBlogger.config.JwtProvider;
import com.hellfire.myBlogger.models.User;
import com.hellfire.myBlogger.request.CreateUserRequest;
import com.hellfire.myBlogger.request.LoginUserRequest;
import com.hellfire.myBlogger.response.CreateUserResponse;
import com.hellfire.myBlogger.response.LogInUserResponse;
import com.hellfire.myBlogger.response.ResponseStatus;
import com.hellfire.myBlogger.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/createUser")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest createUserRequest) {
        CreateUserResponse createUserResponse = new CreateUserResponse();

        try {
            User user = userService.createUser(createUserRequest);
            createUserResponse.setUser(user);
            createUserResponse.setResponseStatus(ResponseStatus.SUCCESS);
            createUserResponse.setToken(getToken(user.getEmail(), user.getPassword()));
        }catch (Exception e) {
            System.out.println(e.getMessage());
            createUserResponse.setResponseStatus(ResponseStatus.FAILURE);
        }

        return createUserResponse;
    }
    @PostMapping("/signIn")
    public LogInUserResponse signIn(@RequestBody LoginUserRequest loginUserRequest) {

        System.out.println("hitting signIn");
        LogInUserResponse loginUserResponse = new LogInUserResponse();

        try {
            User user = userService.signIn(loginUserRequest);
            loginUserResponse.setUser(user);
            loginUserResponse.setResponseStatus(ResponseStatus.SUCCESS);

            // Use the plain text password from the request, NOT the hashed one from the DB
            loginUserResponse.setToken(getToken(user.getEmail(), loginUserRequest.getPassword()));
        } catch (Exception e) {
            System.out.println(e);
            loginUserResponse.setResponseStatus(ResponseStatus.FAILURE);
        }
        return loginUserResponse;
    }



    public String getToken(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            System.out.println("Authentication successful: " + authentication.isAuthenticated());
            return JwtProvider.generateToken(authentication);
        } catch (Exception e) {
            System.out.println("Authentication failed at getToken:  "+e.getMessage() );
//            e.printStackTrace();
            throw e; // Or handle it however you want
        }
    }

}
