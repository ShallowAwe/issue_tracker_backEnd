package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.TeamMember;
import com.rudra.issue_tracker.model.TeamRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    Optional<TeamMember> findByUserIdAndTeamId(Long userId, Long teamId);

    List<TeamMember> findByTeamId(Long teamId);

    List<TeamMember> findByUserId(Long userId);

    boolean existsByUserIdAndTeamId(Long userId, Long teamId);

    boolean existsByTeamIdAndRole(Long teamId, TeamRole role);
}
