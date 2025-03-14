package com.hellfire.myBlogger.services;


import com.hellfire.myBlogger.models.Blog;
import com.hellfire.myBlogger.models.MyUserPrincipal;
import com.hellfire.myBlogger.models.User;
import com.hellfire.myBlogger.repository.BlogRepository;
import com.hellfire.myBlogger.repository.UserRepository;
import com.hellfire.myBlogger.request.CreateUserRequest;
import com.hellfire.myBlogger.request.LoginUserRequest;
import com.hellfire.myBlogger.response.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MyUserDetailService myUserDetailService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    BlogRepository blogRepository;

    public User createUser(CreateUserRequest createUserRequest) throws Exception {
        Optional<User> userOptional=userRepository.findByEmail(createUserRequest.getEmail());
        if(userOptional.isPresent()) {
            throw new Exception("User email already Exist");
        }
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setEmail(createUserRequest.getEmail());
        return userRepository.save(user);
    }

    public UserDTO getUserById(int id)
    {
        UserDTO userDTO=new UserDTO();
        Optional<User> userOptional=userRepository.findById(id);
        if(userOptional.isPresent()) {
            userDTO.setUsername(userOptional.get().getUsername());
            userDTO.setEmail(userOptional.get().getEmail());
            userDTO.setId(userOptional.get().getId());
            List<Blog> userBlogList=blogRepository.findByUser_Email(userOptional.get().getEmail());
            userDTO.setBlogs(userBlogList);
        }
        return userDTO;
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOList= new ArrayList<UserDTO>();
        List<User> userList=userRepository.findAll();
        for(User user:userList) {
            UserDTO userDTO=new UserDTO();
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setId(user.getId());
            List<Blog> userBlogList=blogRepository.findByUser_Email(user.getEmail());
            userDTO.setBlogs(userBlogList);
            userDTOList.add(userDTO);

        }
        return userDTOList;


    }



    public User signIn(LoginUserRequest loginUserRequest) throws Exception {
        try {
            System.out.println("hitting authentication");
            System.out.println(loginUserRequest);
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUserRequest.getEmail(),
                            loginUserRequest.getPassword()
                    )
            );
            // Extract user from authentication
            System.out.println("authentication "+authentication);
            MyUserPrincipal userPrincipal = (MyUserPrincipal) authentication.getPrincipal();

            // Extract and return the actual User object
            return userPrincipal.getUser();

        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            throw new Exception("Invalid email or password in signIn", e);
        }
    }

    public  User getUserByEmail(String email) throws Exception {
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isPresent()) {
            return userOptional.get();
        }else {
            throw new Exception("User with" +email +"not found");
        }
    }




}
