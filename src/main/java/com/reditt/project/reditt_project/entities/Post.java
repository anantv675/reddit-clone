package com.reditt.project.reditt_project.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.Instant;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Builder // generates builder methods for our class
@Entity
@Data // generates getters and setters
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @NotNull(message = "Post name cannot be empty or null") //during validation, this message will be thrown if field is left blank
    private String postName;

    @Nullable
    private String url;

    @Nullable
    @Lob // as description can have large chunk of text
    private String description;

    private Integer voteCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="userId",referencedColumnName = "userId")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="id",referencedColumnName = "id")
    private Subreddit subreddit;

    private Instant createdDate;
}
