package com.rudra.issue_tracker.service;

import com.rudra.issue_tracker.model.*;
import com.rudra.issue_tracker.repository.ProjectTeamRepository;
import com.rudra.issue_tracker.repository.TeamMemberRepository;
import com.rudra.issue_tracker.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ProjectTeamRepository projectTeamRepository;
    private final UserService userService;
    private final ProjectService projectService;

    //Create Team././/.///./
    @Transactional
    public Team createTeam(String name,String description){
        return teamRepository.save(Team.builder().name(name).description(description).build());
    }


    //Add member to the team....
    @Transactional
    public TeamMember addMember(Long teamId, Long userId, String role) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        User user = userService.findById(userId);
        return teamMemberRepository.save(TeamMember.builder().team(team)
                .user(user)
                .roleInTeam(role)
                .build());

    }

    //assign Project to a Team.....
    @Transactional
    public ProjectTeam assignToProject(Long teamId, String projectKey) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));

        Project project = projectService.getByKey(projectKey);

        return projectTeamRepository.save(
                ProjectTeam.builder()
                        .project(project)
                        .team(team)
                        .build()
        );

    }

}
