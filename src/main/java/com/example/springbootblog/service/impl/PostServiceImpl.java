package com.example.springbootblog.service.impl;

import com.example.springbootblog.entity.Post;
import com.example.springbootblog.exception.ResourceNotFoundException;
import com.example.springbootblog.payload.PostDto;
import com.example.springbootblog.repository.PostRepository;
import com.example.springbootblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        //convert dto to entity
        Post post= maptoEntity(postDto);
        Post newPost= postRepository.save(post);

       //convert entityy to dto
        PostDto postResponse=maptoDTO(newPost);
        return postResponse;
    }

    @Override
    public List<PostDto> getAllPosts(int pageNo, int pageSize) {

        //create pageable instance
        Pageable pageable = (Pageable) PageRequest.of(pageNo, pageSize);

        Page<Post> posts= postRepository.findAll((org.springframework.data.domain.Pageable) pageable);

        //get content for page object
        List<Post> listofPosts= posts.getContent();

        return listofPosts.stream().map(post->maptoDTO(post)).collect(Collectors.toList());

    }

    @Override
    public PostDto getPostById(long id) {
        Post post= postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
        return maptoDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        //get post by id from the database..
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost=postRepository.save(post);
        return maptoDTO((updatedPost));
    }

    @Override
    public void deletePostById(long id) {
       Post post= postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id)) ;
       postRepository.delete(post);
    }


    //METHOD CONVERTING ENTITY TO DTO
    private PostDto maptoDTO(Post post){
        PostDto postDto= new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return postDto;
    }

    private Post maptoEntity(PostDto postDto){
        Post post= new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }
}
