package com.reditt.project.reditt_project.Repositories;

import com.reditt.project.reditt_project.entities.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.print.DocFlavor;
import java.util.Optional;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit,Long> {

    Optional<Subreddit> findByName(String name);
}