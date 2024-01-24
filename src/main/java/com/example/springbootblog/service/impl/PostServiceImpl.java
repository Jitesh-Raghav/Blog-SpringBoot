package com.example.springbootblog.service.impl;

import com.example.springbootblog.entity.Category;
import com.example.springbootblog.entity.Post;
import com.example.springbootblog.exception.ResourceNotFoundException;
import com.example.springbootblog.payload.PostDto;
import com.example.springbootblog.payload.PostResponse;
import com.example.springbootblog.repository.CategoryRepository;
import com.example.springbootblog.repository.PostRepository;
import com.example.springbootblog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;

    private CategoryRepository categoryRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper,CategoryRepository categoryRepository) {

        this.postRepository = postRepository;
        this.mapper=mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));

        //convert dto to entity
        Post post= maptoEntity(postDto);
        post.setCategory(category);
        Post newPost= postRepository.save(post);

       //convert entityy to dto
        PostDto postResponse=maptoDTO(newPost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort= sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        //create pageable instance
        Pageable pageable = (Pageable) PageRequest.of(pageNo, pageSize, sort);   //Add .descending to sort in descending order

        Page<Post> posts= postRepository.findAll(pageable);

        //get content for page object
        List<Post> listofPosts= posts.getContent();

        List<PostDto> content=  listofPosts.stream().map(post->maptoDTO(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();

        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
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

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);

        Post updatedPost=postRepository.save(post);
        return maptoDTO((updatedPost));
    }

    @Override
    public void deletePostById(long id) {
       Post post= postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id)) ;
       postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts.stream().map((post) -> maptoDTO(post))
                .collect(Collectors.toList());
    }


    //METHOD CONVERTING ENTITY TO DTO
    private PostDto maptoDTO(Post post){
        PostDto postDto = mapper.map(post, PostDto.class);
//        PostDto postDto= new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }

    private Post maptoEntity(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
//        Post post= new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
}
