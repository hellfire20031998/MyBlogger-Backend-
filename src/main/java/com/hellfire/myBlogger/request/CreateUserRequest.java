package com.hellfire.myBlogger.request;

import jakarta.annotation.Nonnull;
import jakarta.persistence.JoinColumn;
import lombok.Data;


@Data
public class CreateUserRequest {

    private String username;

    private String password;

    private String email;
}
