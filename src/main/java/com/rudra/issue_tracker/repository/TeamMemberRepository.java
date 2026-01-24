package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Team;
import com.rudra.issue_tracker.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByTeam(Team team);
}
