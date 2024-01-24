package com.example.springbootblog.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private long id;

    //title should not be null and should have atlest 2 characters
    @NotEmpty
    @Size(min=2, message = "Post title should have atleast 2 characters")
    private String title;

    //title should not be null and should have atlest 10 characters
    @NotEmpty
    @Size(min=10, message = "Post description should have atleast 10 characters")
    private String description;

    @NotEmpty
    private String content;
    private Set<CommentDto> comments;

    private Long CategoryId;

}
