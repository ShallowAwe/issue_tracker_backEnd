package com.rudra.issue_tracker.controller;

import com.rudra.issue_tracker.model.Team;
import com.rudra.issue_tracker.model.TeamMember;
import com.rudra.issue_tracker.service.TeamService;
import com.rudra.issue_tracker.controller.dto.CreateTeamRequest;
import com.rudra.issue_tracker.controller.dto.AddMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    // ✅ Create Team
    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody CreateTeamRequest request) {

        Team team = teamService.createTeam(
                request.getName(),
                request.getDescription(),
                request.getCreatorUserId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(team);
    }

    // ✅ Add Member to Team
    @PostMapping("/{teamId}/members")
    public ResponseEntity<TeamMember> addMember(
            @PathVariable Long teamId,
            @RequestBody AddMemberRequest request
    ) {

        TeamMember member = teamService.addTeamMember(
                teamId,
                request.getUserId(),
                request.getRole(),
                request.getActionByUserId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }
}
