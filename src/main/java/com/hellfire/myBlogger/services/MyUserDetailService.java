package com.hellfire.myBlogger.services;

import com.hellfire.myBlogger.models.MyUserPrincipal;
import com.hellfire.myBlogger.models.User;
import com.hellfire.myBlogger.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername called for: " + userEmail);
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isPresent()) {
            System.out.println("User found: " + user.get().getEmail());
            return new MyUserPrincipal(user.get());
        }
        throw new UsernameNotFoundException("User not found");
    }
}
