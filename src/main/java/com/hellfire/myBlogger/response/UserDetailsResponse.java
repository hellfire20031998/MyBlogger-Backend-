package com.hellfire.myBlogger.response;


import lombok.Data;

@Data
public class UserDetailsResponse {

    private String UserName;
    private String email;
    private ResponseStatus status;
}
