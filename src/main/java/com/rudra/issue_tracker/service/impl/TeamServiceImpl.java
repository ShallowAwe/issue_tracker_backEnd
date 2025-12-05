package com.rudra.issue_tracker.service.impl;

import com.rudra.issue_tracker.model.Team;
import com.rudra.issue_tracker.model.TeamMember;
import com.rudra.issue_tracker.model.TeamRole;
import com.rudra.issue_tracker.repository.TeamMemberRepository;
import com.rudra.issue_tracker.repository.TeamRepository;
import com.rudra.issue_tracker.repository.UserRepository;
import com.rudra.issue_tracker.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;


    @Override
    public Team createTeam(String name, String description, Long creatorUserId) {

        if (creatorUserId == null) {
            throw new RuntimeException("creatorUserId must not be null");
        }

        if (!userRepository.existsById(creatorUserId)) {
            throw new RuntimeException("Creator user does not exist");
        }

        if (teamRepository.existsByName(name)) {
            throw new RuntimeException("Team name already exists");
        }

        Team team = Team.builder()
                .name(name)
                .description(description)
                .createdBy(creatorUserId)
                .build();

        Team savedTeam = teamRepository.save(team);

        TeamMember teamLead = TeamMember.builder()
                .teamId(savedTeam.getId())
                .userId(creatorUserId)
                .role(TeamRole.TEAM_LEAD)
                .build();

        teamMemberRepository.save(teamLead);

        return savedTeam;
    }

    @Override
    public TeamMember addTeamMember(Long teamId, Long userId, TeamRole role, Long actionByUserId) {

        // ✅ Correct repository to check team
        if (!teamRepository.existsById(teamId)) {
            throw new RuntimeException("Team Does Not Exist");
        }

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User Does Not Exist");
        }

        // ✅ CHECK ADMIN USING ACTION USER
        boolean isAdmin = userRepository.findById(actionByUserId)
                .orElseThrow(() -> new RuntimeException("Action User Not Found"))
                .isAdmin();

        // ✅ CHECK TEAM LEAD USING ACTION USER
        boolean isTeamLead = teamMemberRepository
                .findByUserIdAndTeamId(actionByUserId, teamId)
                .map(m -> m.getRole() == TeamRole.TEAM_LEAD)
                .orElse(false);

        if (!isAdmin && !isTeamLead) {
            throw new RuntimeException("Only TeamLeads and Admins Can Add TeamMembers");
        }

        if (teamMemberRepository.existsByUserIdAndTeamId(userId, teamId)) {
            throw new RuntimeException("User Already Exists In This Team");
        }

        // ✅ Enforce only ONE TeamLead per team
        if (role == TeamRole.TEAM_LEAD &&
                teamMemberRepository.existsByTeamIdAndRole(teamId, TeamRole.TEAM_LEAD)) {
            throw new RuntimeException("This Team Already Has a TeamLead");
        }

        TeamMember member = TeamMember.builder()
                .teamId(teamId)
                .userId(userId)
                .role(role)
                .build();

        return teamMemberRepository.save(member);
    }

}
