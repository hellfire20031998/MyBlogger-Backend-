package com.hellfire.myBlogger.repository;

import com.hellfire.myBlogger.models.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

    void deleteById(int id);

    List<Blog> findByUser_Id(int userId);

    List<Blog> findByUser_Email(String userEmail);
}
