// SprintService.java
package com.rudra.issue_tracker.service;

import com.rudra.issue_tracker.exceptions.NotFoundException;
import com.rudra.issue_tracker.model.Project;
import com.rudra.issue_tracker.model.Sprint;
import com.rudra.issue_tracker.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SprintService {

    private final SprintRepository sprintRepository;
    private final ProjectService projectService;

    @Transactional
    public Sprint createSprint(String projectKey, String name, LocalDate start, LocalDate end, String goal) {
        Project project = projectService.getByKey(projectKey);

        Sprint sprint = Sprint.builder()
                .project(project)
                .name(name)
                .startDate(start)
                .endDate(end)
                .goal(goal)
                .status("PLANNED")
                .build();

        return sprintRepository.save(sprint);
    }

    @Transactional(readOnly = true)
    public List<Sprint> getSprintsByProject(String projectKey) {
        Project project = projectService.getByKey(projectKey);
        return sprintRepository.findByProject(project);
    }

    @Transactional
    public Sprint changeStatus(Long sprintId, String status) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new NotFoundException("Sprint not found: " + sprintId));
        sprint.setStatus(status);
        return sprintRepository.save(sprint);
    }
}
