package com.reditt.project.reditt_project.SubredditMapper;

import com.reditt.project.reditt_project.dto.PostRequest;
import com.reditt.project.reditt_project.dto.PostResponse;
import com.reditt.project.reditt_project.entities.Post;
import com.reditt.project.reditt_project.entities.Subreddit;
import com.reditt.project.reditt_project.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdDate",expression = "java(java.time.Instant.now())")
    @Mapping(target = "description",expression = "java(postRequest.getDescription())")
    public Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id" , expression = "java(post.getPostId())")
    @Mapping(target = "postName" , expression = "java(post.getPostName())") // if the target and expression are same, they can be removed
    @Mapping(target = "url" , expression = "java(post.getUrl())")
    @Mapping(target = "description" , expression = "java(post.getDescription())")
    @Mapping(target = "subredditName" , expression = "java(post.getSubreddit().getName())")
    @Mapping(target = "userName" , expression = "java(post.getUser().getUserName())")
    public PostResponse mapToDto(Post post);
}
