package com.example.springbootblog.service.impl;

import com.example.springbootblog.entity.Comment;
import com.example.springbootblog.entity.Post;
import com.example.springbootblog.exception.BlogAPIException;
import com.example.springbootblog.exception.ResourceNotFoundException;
import com.example.springbootblog.payload.CommentDto;
import com.example.springbootblog.repository.CommentRepository;
import com.example.springbootblog.repository.PostRepository;
import com.example.springbootblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.postRepository= postRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        //retrieve post entity by id
        Post post= postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post", "id", postId));

        //comment entity to comment entity
        comment.setPost(post);

        //comment entity to DB
        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
       List<Comment> comments = commentRepository.findByPostId(postId);

       //convert list of comment entities to list of comment Dto's
       return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        //retrieve post entity by id
        Post post= postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post", "id", postId));

        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {

        //retrieve post entity by id
        Post post= postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post", "id", postId));

        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment=commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {

        //retrieve post entity by id
        Post post= postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post", "id", postId));

        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

       commentRepository.delete(comment);
    }

    //HERE ALSO MODEL MAPPER CAN BE USED FOR CONVERSION JUST LIKE IN POST SERVICE
    private CommentDto mapToDTO(Comment comment){
        CommentDto commentDto= new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;
    }
}
