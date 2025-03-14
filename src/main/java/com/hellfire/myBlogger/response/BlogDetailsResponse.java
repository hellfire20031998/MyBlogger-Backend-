package com.hellfire.myBlogger.response;

import com.hellfire.myBlogger.models.Blog;
import lombok.Data;

@Data
public class BlogDetailsResponse {
    private Blog blog;
    private ResponseStatus status;
}
