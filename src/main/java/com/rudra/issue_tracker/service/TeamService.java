package com.rudra.issue_tracker.service;

import com.rudra.issue_tracker.model.Team;
import com.rudra.issue_tracker.model.TeamMember;
import com.rudra.issue_tracker.model.TeamRole;

public interface TeamService {


    Team createTeam(String name, String description,Long creatorUserId);
    TeamMember addTeamMember(Long teamId, Long userId, TeamRole role,Long actionByUserId);
}
