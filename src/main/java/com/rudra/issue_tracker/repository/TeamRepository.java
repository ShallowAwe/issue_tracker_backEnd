package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(String name);

}
