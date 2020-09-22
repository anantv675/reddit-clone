package com.reditt.project.reditt_project.controllers;

import com.reditt.project.reditt_project.dto.PostRequest;
import com.reditt.project.reditt_project.dto.PostResponse;
import com.reditt.project.reditt_project.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody PostRequest postRequest){
        System.out.printf("inside create post");
        postService.save(postRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @GetMapping("/")
    public List<PostResponse> getAllPosts(){
        System.out.printf("inside  getAllPosts()");
        return postService.getAllPosts();
    }

    @GetMapping("/by-subreddit/{id}")
    public List<PostResponse> getAllPostsBySubreddit(@PathVariable Long id){
        return postService.getAllPostsBySubredditId(id);
    }

    @GetMapping("/by-user/{name}")
    public List<PostResponse> getAllPostsByUserName(@PathVariable String userName){
        return postService.getPostsByUserName(userName);

    }
}
