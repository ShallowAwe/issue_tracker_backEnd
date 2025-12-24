package com.rudra.issue_tracker.service;

import com.rudra.issue_tracker.model.Issue;
import com.rudra.issue_tracker.model.Project;
import com.rudra.issue_tracker.model.Sprint;
import com.rudra.issue_tracker.repository.IssueRepository;
import com.rudra.issue_tracker.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final IssueRepository issueRepository;
    private final ProjectService projectService;
    private final SprintRepository sprintRepository;

    public Map<String, List<Issue>> getProjectBoard(String projectKey) {
        Project project = projectService.getByKey(projectKey);
        List<Issue> issues = issueRepository.findByProject(project);
        return groupByStatus(issues);
    }

    public Map<String, List<Issue>> getSprintBoard(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found: " + sprintId));
        List<Issue> issues = issueRepository.findBySprint(sprint);
        return groupByStatus(issues);
    }

    private Map<String, List<Issue>> groupByStatus(List<Issue> issues) {
        return issues.stream()
                .collect(Collectors.groupingBy(
                        i -> i.getStatus().getName(), // e.g. "TO_DO", "IN_PROGRESS"
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }
}
