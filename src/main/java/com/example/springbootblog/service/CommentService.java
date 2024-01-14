package com.example.springbootblog.service;

import com.example.springbootblog.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);
    List<CommentDto> getCommentsByPostId(long postId);
    CommentDto getCommentById(Long postId, Long commentId);  //see difference between long and Long here, long is primitive data type, Long is java class, can have null values,so ab samajh ja, here you are searching so you can  put null values of id, but not in above case...
    CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest);
    void deleteComment(Long postId, Long commentId);


}
