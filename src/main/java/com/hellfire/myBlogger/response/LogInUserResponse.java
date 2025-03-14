package com.hellfire.myBlogger.response;

import com.hellfire.myBlogger.models.User;
import lombok.Data;

@Data
public class LogInUserResponse {
    private User user;
    private String token;
    private ResponseStatus responseStatus;
}
