package com.example.springbootblog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Schema(
        description = "PostDto Model Information"
)
@Data
public class PostDto {
    private long id;


    @Schema(
            description = "Blog Post Title"
    )
    //title should not be null and should have atlest 2 characters
    @NotEmpty
    @Size(min=2, message = "Post title should have atleast 2 characters")
    private String title;



    @Schema(
            description = "Blog Post Description"
    )
    //title should not be null and should have atlest 10 characters
    @NotEmpty
    @Size(min=10, message = "Post description should have atleast 10 characters")
    private String description;


    @Schema(
            description = "Blog Post Content"
    )
    @NotEmpty
    private String content;
    private Set<CommentDto> comments;


    @Schema(
            description = "Blog Post CategoryId"
    )
    private Long CategoryId;

}
