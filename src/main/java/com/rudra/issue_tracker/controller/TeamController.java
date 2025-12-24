package com.rudra.issue_tracker.controller;

import com.rudra.issue_tracker.controller.dto.CreateTeamRequest;
import com.rudra.issue_tracker.controller.dto.ErrorResponse;
import com.rudra.issue_tracker.controller.dto.ProjectAssignRequest;
import com.rudra.issue_tracker.controller.dto.TeamMemberRequest;
import com.rudra.issue_tracker.model.*;
import com.rudra.issue_tracker.repository.ProjectRepository;
import com.rudra.issue_tracker.repository.TeamMemberRepository;
import com.rudra.issue_tracker.repository.TeamRepository;
import com.rudra.issue_tracker.repository.UserRepository;
import com.rudra.issue_tracker.service.TeamService;
import com.rudra.issue_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;


@RestController
@RequestMapping("api/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    // creating team here.....🥹
    @PostMapping("create_team")
    public ResponseEntity<Team> createTeam(@Validated @RequestBody CreateTeamRequest request){
            Team teamRequest = teamService.createTeam(
                    request.getName(),
                    request.getDescription()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(teamRequest);
    }


    //assign a member to the team
    @PostMapping("assignMember_team")
    public ResponseEntity<TeamMember> assignMemberToTeam(@Validated @RequestBody TeamMemberRequest request){
        TeamMember teamMember = teamService.addMember(
                request.getTeamId(),
                request.getUserId(),
                request.getRole()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(teamMember);
    }

    // assign  project to  a Team...........
   @PostMapping("assign_project")
   public ResponseEntity<ProjectTeam> assignProject(@Validated @RequestBody ProjectAssignRequest request){
        ProjectTeam ProjectTeam = teamService.assignToProject(
                request.getTeamId(),
                request.getProjectKey()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectTeam);
    }


}
