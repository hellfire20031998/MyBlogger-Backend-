package com.hellfire.myBlogger.response;

import com.hellfire.myBlogger.models.User;
import lombok.Data;

@Data
public class CreateUserResponse {
    private User user;
    private ResponseStatus responseStatus;
    private String token;
}
