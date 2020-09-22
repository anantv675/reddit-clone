package com.reditt.project.reditt_project.services;

import com.reditt.project.reditt_project.Repositories.PostRepository;
import com.reditt.project.reditt_project.Repositories.SubredditRepository;
import com.reditt.project.reditt_project.Repositories.UserRepository;
import com.reditt.project.reditt_project.SubredditMapper.PostMapper;
import com.reditt.project.reditt_project.dto.PostRequest;
import com.reditt.project.reditt_project.dto.PostResponse;
import com.reditt.project.reditt_project.entities.Post;
import com.reditt.project.reditt_project.entities.Subreddit;
import com.reditt.project.reditt_project.entities.User;
import com.reditt.project.reditt_project.exceptions.SpringRedittException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final SubredditRepository subredditRepository;
    private final PostMapper postMapper ;

    @Transactional(readOnly = true)
    public Post save(PostRequest postRequest){
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SpringRedittException("No post found by name: " + postRequest.getSubredditName()));
        User currentUser = authService.getCurrentUser();
        return postMapper.map(postRequest,subreddit,currentUser);
    }

    @Transactional(readOnly = true)
    public  PostResponse getPost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SpringRedittException("Post with the following id " + id + " doesn't exist"));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(){
        System.out.println("getAllPosts service");
        return postRepository.findAll().stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPostsBySubredditId(Long id) {
        Optional<Subreddit> subredditOptional =
                Optional.ofNullable(subredditRepository.findById(id).orElseThrow(() -> new SpringRedittException("No subreddit found")));
        Subreddit subreddit = subredditOptional.get();
        List<Post> allPostsBySubreddit = postRepository.findAllBySubreddit(subreddit);
        return allPostsBySubreddit.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUserName(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new SpringRedittException("No post found for user name:" + userName));
        return postRepository.findByUser(user).stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }
}
