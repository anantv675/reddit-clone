package com.reditt.project.reditt_project.services;

import com.reditt.project.reditt_project.Repositories.SubredditRepository;
import com.reditt.project.reditt_project.SubredditMapper.SubredditMapper;
import com.reditt.project.reditt_project.dto.SubredditDto;
import com.reditt.project.reditt_project.entities.Subreddit;
import com.reditt.project.reditt_project.exceptions.SpringRedittException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;

    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto).collect(Collectors.toList());
    }


    public SubredditDto getSubreddit(Long id){
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(()-> new SpringRedittException("No Subreddit found with the following id: "+id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }

}
