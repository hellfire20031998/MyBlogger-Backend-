package com.hellfire.myBlogger.config;

import com.hellfire.myBlogger.services.JWTService;
import com.hellfire.myBlogger.services.MyUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenValidator extends OncePerRequestFilter {

    @Autowired
    ApplicationContext context;

    @Autowired
    private JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt=request.getHeader(JwtConstant.JWT_HEADER);
        if(jwt!=null){

            try{
                String email=JwtProvider.getEmailFromJwtToken(jwt);

                List<GrantedAuthority> auths=new ArrayList<>();
                Authentication authentication=new UsernamePasswordAuthenticationToken(email,null,auths);
                SecurityContextHolder.getContext().setAuthentication(authentication);



            }catch (Exception e){
                throw new BadCredentialsException("Invalid JWT token provided");
            }
        }

        filterChain.doFilter(request, response);
    }
}
