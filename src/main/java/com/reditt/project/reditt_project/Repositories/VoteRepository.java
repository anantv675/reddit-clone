package com.reditt.project.reditt_project.Repositories;

import com.reditt.project.reditt_project.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
}
