package com.example.springbootblog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", nullable=false)  //POST_ID IS FOREIGN KEY IN COMMENT TABLE, USKE LIYE HAI YE, MANYTOONE RELATIONSHIP KE LIYE HAI YE
    private Post post;

}
