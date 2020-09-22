package com.reditt.project.reditt_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SubredditDto {

    private Long id;
    private String name;
    private String description;
    private int numberOfPosts;
}
