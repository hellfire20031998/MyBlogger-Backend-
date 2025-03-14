package com.hellfire.myBlogger.services;


import com.hellfire.myBlogger.models.Blog;
import com.hellfire.myBlogger.models.User;
import com.hellfire.myBlogger.repository.BlogRepository;
import com.hellfire.myBlogger.repository.UserRepository;
import com.hellfire.myBlogger.request.CreateBlogRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Blog createBlog(CreateBlogRequest createBlogRequest) throws Exception {
        Optional<User> user = userRepository.findById(createBlogRequest.getUserId());
        System.out.println(createBlogRequest.getEmail());
        if(user.isEmpty()) {
            throw new Exception("User not Found");
        }
        Blog blog = new Blog();
        blog.setUser(user.get());
        blog.setTitle(createBlogRequest.getTitle());
        blog.setAuthor(createBlogRequest.getAuthor());
        blog.setPublishDate(new Date());
        blog.setUpdatedAt(new Date());
        blog.setContent(createBlogRequest.getContent());
        return blogRepository.save(blog);
    }
    @Transactional
    public void deleteBlog(int id) throws Exception {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if (blogOptional.isPresent()) {
            Blog blog = blogOptional.get();
            User user = blog.getUser();

            // Remove the blog from the user's list of blogs
            user.getBlogs().remove(blog);

            blogRepository.delete(blog);
            System.out.println("Blog successfully deleted");
        } else {
            throw new Exception("Blog Not Found");
        }
    }



    public Blog updateBlog(CreateBlogRequest createBlogRequest,int blogId) throws Exception {
        Optional<User> user = userRepository.findByEmail(createBlogRequest.getEmail());
        System.out.println(createBlogRequest.getEmail());
        if(user.isEmpty()) {
            throw new Exception("User not Found");
        }
        List<Blog> allBlogs = blogRepository.findByUser_Email(user.get().getEmail());

       Optional<Blog> blogOptional =allBlogs.stream().filter(blog1 -> blog1.getId() == blogId).findFirst();
       if(blogOptional.isEmpty()){
           throw new Exception("Blog Not Found");
       }
       Blog blog = blogOptional.get();
        blog.setUser(user.get());
        blog.setTitle(createBlogRequest.getTitle());
        blog.setAuthor(createBlogRequest.getAuthor());
        blog.setContent(createBlogRequest.getContent());
        blog.setUpdatedAt(new Date());
        return blogRepository.save(blog);
    }

    public List<Blog> getAllBlogs(){
        return blogRepository.findAll();
    }

    public Blog getBlogById(int id) throws Exception {
        Optional<Blog> blogOptional=blogRepository.findById(id);
        if(blogOptional.isPresent()) {
            return blogOptional.get();
        }
        throw new Exception("Blog Not Found");
    }
    public List<Blog> getBlogByEmail(String email) throws Exception {
         List<Blog> blogs=  blogRepository.findByUser_Email(email);

         if(blogs.isEmpty()) {
             throw new Exception("Blog Not Found");
         }


        return blogs;
    }


}
