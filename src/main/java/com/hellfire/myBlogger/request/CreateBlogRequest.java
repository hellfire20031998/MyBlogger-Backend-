package com.hellfire.myBlogger.request;


import lombok.Data;

@Data
public class CreateBlogRequest {

    private int UserId;
    private String email;
    private String title;
    private String content;
    private String author;

}
