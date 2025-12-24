package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Team;
import com.rudra.issue_tracker.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByTeam(Team team);
}
