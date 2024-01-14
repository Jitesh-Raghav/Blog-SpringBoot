package com.example.springbootblog.repository;

import com.example.springbootblog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  //optional
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByPostId(long id);
}
