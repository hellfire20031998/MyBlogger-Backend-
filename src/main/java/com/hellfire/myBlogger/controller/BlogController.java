package com.hellfire.myBlogger.controller;


import com.hellfire.myBlogger.config.JwtProvider;
import com.hellfire.myBlogger.models.Blog;
import com.hellfire.myBlogger.models.User;
import com.hellfire.myBlogger.request.CreateBlogRequest;
import com.hellfire.myBlogger.response.BlogDTO;
import com.hellfire.myBlogger.response.BlogDetailsResponse;
import com.hellfire.myBlogger.response.ResponseStatus;
import com.hellfire.myBlogger.services.BlogService;
import com.hellfire.myBlogger.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;

    @PostMapping("/createBlog/")
    public Blog createBlog(@RequestBody CreateBlogRequest createBlogRequest,
                           @RequestHeader("Authorization") String token ) {
        System.out.println(token);

        String email = JwtProvider.getEmailFromJwtToken(token);
        System.out.println(email);
        User user=null;
        Blog blog=null;
        try {
           user=userService.getUserByEmail(email);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            createBlogRequest.setUserId(user.getId());
           blog= blogService.createBlog(createBlogRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return blog;
    }

    @GetMapping("/blogs")
    public List<Blog> getAllBlogs() {
//        System.out.println(token);
        return blogService.getAllBlogs();
    }
    @GetMapping("/getBlog/{id}")
    public BlogDetailsResponse getBlogById(@RequestHeader("Authorization") String token,
                                           @PathVariable int id) {
        BlogDetailsResponse blogDetailsResponse=new BlogDetailsResponse();
        String email = JwtProvider.getEmailFromJwtToken(token);

        try {
//            User user=userService.getUserByEmail(email);
            Blog blog=blogService.getBlogById(id);
            blogDetailsResponse.setBlog(blog);
            blogDetailsResponse.setStatus(ResponseStatus.SUCCESS);

        }catch (Exception e) {
            System.out.println(e.getMessage());
            blogDetailsResponse.setStatus(ResponseStatus.FAILURE);
        }
        return blogDetailsResponse;
    }
    @GetMapping("/getBlogEmail/")
    public List<Blog> getBlogByEmail(@RequestHeader("Authorization")String token) {

        String email = JwtProvider.getEmailFromJwtToken(token);

       try {

           return blogService.getBlogByEmail(email);
       }catch (Exception e){
           System.out.println(e.getMessage());
       }
      return null;
    }

    @DeleteMapping("/deleteBlog/{id}")
    public ResponseStatus deleteBlogById(@RequestHeader("Authorization") String token,
                                         @PathVariable int id) {
        String email = JwtProvider.getEmailFromJwtToken(token);
        System.out.println("email for deleteBlogById: "+email);
        try{
           blogService.deleteBlog(id);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseStatus.FAILURE;
        }


        return ResponseStatus.SUCCESS;
    }
//    @GetMapping("/getBlog/{id}")
//    public BlogDTO getBlogById(@RequestHeader("Authorization") String token,
//                               @PathVariable int id) {
//        BlogDTO blogDTO=new BlogDTO();
//        System.out.println("Getting blog by id: "+id);
//
//        String email = JwtProvider.getEmailFromJwtToken(token);
//        try {
//            User user=userService.getUserByEmail(email);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        try {
//            Blog blog= blogService.getBlogById(id);
//            blogDTO.setId(blog.getId());
//
//            blogDTO.setAuthor(blog.getAuthor());
//            blogDTO.setTitle(blog.getTitle());
//            blogDTO.setContent(blog.getContent());
//            blogDTO.setPublishDate(blog.getPublishDate());
//            System.out.println(blog);
//        }catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return blogDTO;
//
//    }

    @PutMapping("/updateBlog/{id}")
    public BlogDetailsResponse updateBlog(@RequestHeader("Authorization") String token,
                                          @RequestBody CreateBlogRequest createBlogRequest,
                                          @PathVariable int id) {
        BlogDetailsResponse blogDetailsResponse=new BlogDetailsResponse();
        String email = JwtProvider.getEmailFromJwtToken(token);
        System.out.println("email for updateBlogById: "+email);
        try{
//            User user=userService.getUserByEmail(email);
            createBlogRequest.setEmail(email);
            Blog updatedBlog=blogService.updateBlog(createBlogRequest,id);
            blogDetailsResponse.setBlog(updatedBlog);
            System.out.println(updatedBlog);
            blogDetailsResponse.setStatus(ResponseStatus.SUCCESS);
        }catch (Exception e){
            blogDetailsResponse.setStatus(ResponseStatus.FAILURE);
            System.out.println(e.getMessage());
        }
        return blogDetailsResponse;
    }

}
